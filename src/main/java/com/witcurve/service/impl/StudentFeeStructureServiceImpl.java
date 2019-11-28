package com.witcurve.service.impl;

import com.witcurve.domain.SessionFeeDescription;
import com.witcurve.domain.SessionFeeStructure;
import com.witcurve.domain.StudentFeeStructure;
import com.witcurve.domain.StudentStandard;
import com.witcurve.repository.FeeDetailsRepository;
import com.witcurve.repository.SessionFeeStructureRepository;
import com.witcurve.repository.StudentFeeStructureRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.service.StudentFeeStructureService;
import com.witcurve.service.dto.StudentFeeDescriptionDTO;
import com.witcurve.service.dto.StudentFeeStructureDTO;
import com.witcurve.service.dto.StudentFeeTypeDTO;
import com.witcurve.service.mapper.StudentFeeStructureMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentFeeStructureServiceImpl implements StudentFeeStructureService {

    private final Logger log = LoggerFactory.getLogger(StudentFeeStructureServiceImpl.class);

    @Autowired
    StudentFeeStructureRepository studentfeeStructureRepository;

    @Autowired
    StudentFeeStructureMapper studentFeeStructureMapper;

    @Autowired
    FeeDetailsRepository feeDetailsRepository;

    @Autowired
    SessionFeeStructureRepository sessionFeeStructureRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Override
    public StudentFeeStructureDTO saveOrUpdate(StudentFeeStructureDTO studentFeeStructureDTO) {
        log.debug("Request to save or update StudentFeeStructure : {} ", studentFeeStructureDTO);
        isValid(studentFeeStructureDTO);
        StudentFeeStructure studentFeeStructure = studentfeeStructureRepository.save(studentFeeStructureMapper.toEntity(studentFeeStructureDTO));
        return studentFeeStructureMapper.toDto(studentFeeStructure);
    }

    @Override
    public StudentFeeStructureDTO getByStudentIdAndSessionId(Long studentId, Long sessionId) {
        log.debug("Request to get studentFeeStructure by studentId and sessionId");
        StudentFeeStructure studentFeeStructure = studentfeeStructureRepository.getByStudentIdAndSessionId(studentId, sessionId);
        return studentFeeStructureMapper.toDto(studentFeeStructure);
    }

    @Override
    public List<StudentFeeStructureDTO> getByStandardIdAndSessionId(Long standardId, Long sessionId) {
        log.debug("Request to get studentFeeStructure by standardId and sessionId");
        List<StudentFeeStructure> studentFeeStructures = studentfeeStructureRepository.getByStandardIdAndSessionId(standardId, sessionId);
        return studentFeeStructureMapper.toDto(studentFeeStructures);
    }

    private void isValid(StudentFeeStructureDTO studentFeeStructureDTO) {

        if (studentFeeStructureDTO.getStudentFeeTypes().size() == 0) {
            throw new WitcurveException("Minimum one record of student fee type is require to save ");
        }
        List<StudentStandard> studentStandard = studentStandardRepository.getByStudentId(studentFeeStructureDTO.getStudentId());
        List<SessionFeeStructure> sessionFeeStructures = sessionFeeStructureRepository.findByGradeAndSessionId(studentStandard.get(0).getStandard().getGrade(), studentFeeStructureDTO.getSelectedSessionId());

        Map<Long, List<Long>> mapOfFeeTypeAndRequiredFeeDescriptionIds = new HashMap<>();
        Map<Long, List<Long>> mapOfAllFeeTypeAndFeeDescriptionIds = new HashMap<>();
        for (SessionFeeStructure sessionFeeStructure : sessionFeeStructures) {
            Long feeTypeId = sessionFeeStructure.getFeeType().getId();
            List<Long> requireFeeDescriptionIds = new ArrayList<>();
            List<Long> allFeeDescriptionIds = new ArrayList<>();

            for (SessionFeeDescription sessionFeeDescription : sessionFeeStructure.getSessionFeeDescriptions()) {
                allFeeDescriptionIds.add(sessionFeeDescription.getFeeDescription().getId());
                if (sessionFeeDescription.getRequired() == true) {
                    requireFeeDescriptionIds.add(sessionFeeDescription.getFeeDescription().getId());
                }
            }
            mapOfFeeTypeAndRequiredFeeDescriptionIds.put(feeTypeId, requireFeeDescriptionIds);
            mapOfAllFeeTypeAndFeeDescriptionIds.put(feeTypeId, allFeeDescriptionIds);
        }
        List<Long> studentFeeTypeIds = studentFeeStructureDTO.getStudentFeeTypes().stream().map(StudentFeeTypeDTO::getFeeTypeId).collect(Collectors.toList());
        List<Long> sessionFeeTypeIds = mapOfFeeTypeAndRequiredFeeDescriptionIds.keySet().stream().collect(Collectors.toList());

        if (!sessionFeeTypeIds.containsAll(studentFeeTypeIds)) {
            throw new WitcurveException("Given fee type is not present in session fee type");
        }
        for (StudentFeeTypeDTO studentFeeType : studentFeeStructureDTO.getStudentFeeTypes()) {

            if (studentFeeType.getStudentFeeDescriptions().size() == 0) {
                throw new WitcurveException("Minimum one record of student fee description is require to save ");
            }
            List<Long> studentFeeDescriptionIds = studentFeeType.getStudentFeeDescriptions().stream().map(StudentFeeDescriptionDTO::getFeeDescriptionId).collect(Collectors.toList());
            List<Long> allRequiredFeeDescriptionIds = mapOfFeeTypeAndRequiredFeeDescriptionIds.get(studentFeeType.getFeeTypeId());
            List<Long> allSessionFeeDescriptionIds = mapOfAllFeeTypeAndFeeDescriptionIds.get(studentFeeType.getFeeTypeId());
            if (!studentFeeDescriptionIds.containsAll(allRequiredFeeDescriptionIds)) {
                throw new WitcurveException("All required fee description are not present");
            }
            if (!allSessionFeeDescriptionIds.containsAll(studentFeeDescriptionIds)) {
                throw new WitcurveException("Given fee description id is not present in session fee type");
            }
            for (StudentFeeDescriptionDTO studentFeeDescription : studentFeeType.getStudentFeeDescriptions()) {

                if (studentFeeDescription.getAmount() < 0 || studentFeeDescription.getOneTimeDiscount() < 0) {
                    throw new WitcurveException("amount and discount must be positive");
                }
                if (studentFeeDescription.getAmount() > studentFeeDescription.getAdjustment() - (studentFeeDescription.getOneTimeDiscount())) {
                    throw new WitcurveException("Values are improper according to given amount");
                }
            }
        }
    }
}

