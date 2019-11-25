package com.witcurve.service.impl;

import com.witcurve.domain.FeeDetails;
import com.witcurve.domain.StudentFeeStructure;
import com.witcurve.domain.enumeration.FeeDetailsType;
import com.witcurve.repository.FeeDetailsRepository;
import com.witcurve.repository.StudentFeeStructureRepository;
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

import java.util.List;
import java.util.Optional;
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

    @Override
    public StudentFeeStructureDTO saveOrUpdate(StudentFeeStructureDTO studentFeeStructureDTO) {
        log.debug("Request to save or update StudentFeeStructure : {} ", studentFeeStructureDTO);
        if (studentFeeStructureDTO.getStudentFeeTypes().size() == 0) {
            throw new WitcurveException("Minimum one record of student fee type is require to save ");
        }
        for (StudentFeeTypeDTO studentFeeType : studentFeeStructureDTO.getStudentFeeTypes()) {
            Optional<FeeDetails> feeDetailOfFeeType = feeDetailsRepository.findById(studentFeeType.getFeeTypeId());
            if (!feeDetailOfFeeType.isPresent()) {
                throw new WitcurveException("No fee detail is present with given id");
            }
            if (!feeDetailOfFeeType.get().getType().equals(FeeDetailsType.FEE_TYPE)) {
                throw new WitcurveException("Given id is not of fee type");
            }
            List<StudentFeeDescriptionDTO> studentFeeDescriptions = studentFeeType.getStudentFeeDescriptions();

            if (studentFeeDescriptions.size() == 0) {
                throw new WitcurveException("Minimum one record of student fee description is require");
            }
            List<Long> feeDescriptionIds = studentFeeDescriptions.stream().map(StudentFeeDescriptionDTO::getFeeDescriptionId).collect(Collectors.toList());

            List<FeeDetails> feeDescriptions = feeDetailsRepository.findBySchoolInfoIdAndType(feeDetailOfFeeType.get().getSchoolInfo().getId(), FeeDetailsType.FEE_DESCRIPTION);
            List<Long> allFeeDescriptionIds = feeDescriptions.stream().map(FeeDetails::getId).collect(Collectors.toList());

            if (!allFeeDescriptionIds.containsAll(feeDescriptionIds)) {
                throw new WitcurveException("Given id is not of fee description type");
            }
        }
        StudentFeeStructure sfs = studentFeeStructureMapper.toEntity(studentFeeStructureDTO);
        StudentFeeStructure studentFeeStructure = studentfeeStructureRepository.save(sfs);
        return studentFeeStructureMapper.toDto(studentFeeStructure);
    }

    @Override
    public StudentFeeStructureDTO getByStudentIdAndSessionId(Long studentId, Long sessionId) {
        log.debug("Request to get studentFeeStructure");
        StudentFeeStructure studentFeeStructure = studentfeeStructureRepository.getByStudentIdAndSessionId(studentId, sessionId);
        return studentFeeStructureMapper.toDto(studentFeeStructure);
    }


    @Override
    public List<StudentFeeStructureDTO> getByStandardIdAndSessionId(Long standardId, Long sessionId) {
        log.debug("Request to get studentFeeStructure");
        List<StudentFeeStructure> studentFeeStructures = studentfeeStructureRepository.getByStandardIdAndSessionId(standardId, sessionId);
        return studentFeeStructureMapper.toDto(studentFeeStructures);
    }
}

