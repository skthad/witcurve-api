package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.repository.*;
import com.witcurve.service.StandardService;
import com.witcurve.service.dto.StandardDTO;
import com.witcurve.service.mapper.StandardMapper;
import com.witcurve.service.mapper.StandardMapperLite;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {

    private final Logger log  = LoggerFactory.getLogger(StandardServiceImpl.class);

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    StandardMapper standardMapper;

    @Autowired
    StandardMapperLite standardMapperLite;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    GeneralSlotDetailsRepository generalSlotDetailsRepository;

    @Override
    public StandardDTO saveOrUpdateStandard(StandardDTO standardDTO) throws WitcurveException {
        log.debug("Request to save or update Standard: {}", standardDTO);
        standardDTO.setSection(standardDTO.getSection().toUpperCase());
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(standardDTO.getSchoolInfo().getId());
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("SchoolInfo does not exist with given id");
        }
        if(standardDTO.getClassTeacherId() != null) {
            Optional<Staff> staffOptional = staffRepository.findById(standardDTO.getClassTeacherId());
            if(!staffOptional.isPresent()) {
                throw new WitcurveException("No staff exits with given id " + standardDTO.getClassTeacherId());
            }
            Standard existingClassTeacherStandard = standardRepository.findByClassTeacherId(standardDTO.getClassTeacherId());
            if(standardDTO.getId() == null) {
                if(existingClassTeacherStandard != null) {
                    throw new WitcurveException("There exists a standard with class teacher " + standardDTO.getClassTeacherId());
                }
            } else {
                if(existingClassTeacherStandard != null && !existingClassTeacherStandard.getId().equals(standardDTO.getId())) {
                    throw new WitcurveException("There exists a standard with class teacher " + standardDTO.getClassTeacherId());
                }
            }
        }
        if(standardDTO.getId()== null) {
            Standard existingStandard=standardRepository.findByGradeAndSectionAndSchoolInfoId(standardDTO.getGrade(),
                standardDTO.getSection(), standardDTO.getSchoolInfo().getId());
            if(existingStandard != null) {
                if(existingStandard.getActive()) {
                    throw new WitcurveException("There already exists a standard with given grade and section for this board");
                }
                standardDTO.setId(existingStandard.getId());
            }
        }
        Standard standard = standardMapper.toEntity(standardDTO);
        standard.setSchoolInfo(schoolInfo.get());
        standard = standardRepository.save(standard);
        return standardMapper.toDto(standard);
    }

    @Override
    public StandardDTO getStandardById(Long standardId) throws WitcurveException {
        log.debug("Request to get standard with id : {}", standardId);
        Optional<Standard> standard = standardRepository.findById(standardId);
        if (!standard.isPresent()) {
            throw new WitcurveException("No standard exits with given id " + standardId);
        }
        return standardMapperLite.toDto(standard.get());
    }

    @Override
    public StandardDTO addClassTeacher(Long standardId, Long staffId) throws WitcurveException {
        log.debug("Request to add class teacher for staff with id : {} and standard with id : {}", staffId, standardId);
        Optional<Standard> standard = standardRepository.findById(standardId);
        if (!standard.isPresent()) {
            throw new WitcurveException("No standard exits with given id " + standardId);
        }
        Optional<Staff> staffOptional = staffRepository.findById(staffId);
        if(!staffOptional.isPresent()) {
            throw new WitcurveException("No staff exits with given id " + staffId);
        }
        Standard existingClassTeacherStandard = standardRepository.findByClassTeacherId(staffId);
        if(existingClassTeacherStandard != null && !standard.get().equals(existingClassTeacherStandard)) {
            throw new WitcurveException("There exists a standard with class teacher " + staffId);
        }
        standard.get().setClassTeacher(staffOptional.get());
        return standardMapper.toDto(standard.get());
    }

    @Override
    public List<StandardDTO> getStandardsBySchoolInfoId(Long schoolInfoId, Boolean slotAssigned) {
        log.debug("Request to get standards with school info id : {}", schoolInfoId);
        List<Standard> standards;
        if(slotAssigned == null) {
            standards = standardRepository.findBySchoolInfoId(schoolInfoId);
        } else {
            if(slotAssigned) {
                standards = standardRepository.findSlotAssignedBySchoolInfoId(schoolInfoId);
            } else {
                standards = standardRepository.findSlotUnassignedBySchoolInfoId(schoolInfoId);
            }
        }
        return standardMapperLite.toDto(standards);
    }

    @Override
    public List<StandardDTO> getStandardsByTeacherId(Long teacherId) {
        log.debug("Request to get all standards by by teacher id : {}", teacherId);
        List<Standard> result = courseTeacherRepository.findStandardsByTeacherId(teacherId);
        return standardMapperLite.toDto(result);
    }

    @Override
    public void deleteStandard(Long standardId) throws WitcurveException {
        log.debug("Request to delete standard with id {}", standardId);
        Optional<Standard> standard = standardRepository.findById(standardId);
        if (!standard.isPresent()) {
            throw new WitcurveException("No standard exits with given id " + standardId);
        }
        standard.get().setActive(false);
        standard.get().setClassTeacher(null);
        List<StudentStandard> studentStandards = studentStandardRepository.getByStandardId(standardId);
        List<Long> studentIds = studentStandards
            .stream()
            .map(StudentStandard::getStudent)
            .map(s -> s.getId())
            .collect(Collectors.toList());
        if(studentIds.size()!=0){
            studentStandardRepository.deactivateByStudentIds(studentIds);
        }
        Set<Long> standardsList = new HashSet<>();
        standardsList.add(standardId);
        generalSlotDetailsRepository.deactivateSlotDetailsForStandards(standardsList);
    }
}
