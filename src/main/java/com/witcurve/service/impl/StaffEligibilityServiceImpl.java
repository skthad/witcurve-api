package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.MasterSubject;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.StaffEligibility;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.repository.MasterSubjectRepository;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.repository.StaffEligibilityRepository;
import com.witcurve.service.StaffEligibilityService;
import com.witcurve.service.dto.StaffEligibilityDTO;
import com.witcurve.service.mapper.StaffEligibilityMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class StaffEligibilityServiceImpl implements StaffEligibilityService {

    private final Logger log = LoggerFactory.getLogger(StaffEligibilityServiceImpl.class);

    @Autowired
    StaffEligibilityRepository staffEligibilityRepository;

    @Autowired
    StaffEligibilityMapper staffEligibilityMapper;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    MasterSubjectRepository masterSubjectRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Override
    public StaffEligibilityDTO findById(Long staffEligibilityId) throws WitcurveException {
        log.debug("Request to get staffEligibility with id: {}", staffEligibilityId);
        Optional<StaffEligibility> staffEligibility = staffEligibilityRepository.findById(staffEligibilityId);
        if (!staffEligibility.isPresent()){
            throw new WitcurveException("No staffEligibility with given Id " + staffEligibilityId);
        }
        return staffEligibilityMapper.toDto(staffEligibility.get());
    }

    @Override
    public StaffEligibilityDTO saveOrUpdate(StaffEligibilityDTO staffEligibilityDTO) throws WitcurveException {
        log.debug("Request to save or update StaffEligibility", staffEligibilityDTO);

        if (staffEligibilityDTO.getId() != null) {
            StaffEligibility se = staffEligibilityRepository.getOne(staffEligibilityDTO.getId());
            if (!se.getMasterSubject().getName().equals(staffEligibilityDTO.getMasterSubject())
            || !se.getGrade().equals(staffEligibilityDTO.getGrade())
            || !se.getStaff().getId().equals(staffEligibilityDTO.getStaff().getId())) {
                List<CourseTeacher> associatedCourseTeachers = courseTeacherRepository.findByStaffAndSubjectAndGrade(se.getStaff().getId(), se.getMasterSubject(), se.getGrade());
                if (associatedCourseTeachers.size() > 0) {
                    throw new WitcurveException("There are active courses associated with the existing combination");
                }
            }
        }

        StaffEligibility staffEligibility = staffEligibilityMapper.toEntity(staffEligibilityDTO);
        staffEligibility = staffEligibilityRepository.save(staffEligibility);

        return staffEligibilityMapper.toDto(staffEligibility);
    }

    @Override
    public List<StaffEligibilityDTO> getStaffEligibilitysBySchoolInfo(Long schoolInfoId, String subject, Grade grade) throws WitcurveException {
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        List<StaffEligibilityDTO> result = new ArrayList<>();
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No SchoolInfo with given id " + schoolInfo);
        }
        MasterSubject masterSubject = null;
        if (!Strings.isNullOrEmpty(subject)) {
            masterSubject = masterSubjectRepository.findByName(subject);
            if (masterSubject == null) {
                throw new WitcurveException("Invalid subject provided");
            }
        }
        if (masterSubject == null && grade == null) {
            result =  staffEligibilityMapper.toDto(staffEligibilityRepository.findBySchoolInfo(schoolInfoId));
        } else if (masterSubject != null && grade != null) {
            result =  staffEligibilityMapper.toDto(staffEligibilityRepository.findBySchoolInfoAndSubjectAndGrade(schoolInfoId, masterSubject, grade));
        } else if (masterSubject != null) {
            result =  staffEligibilityMapper.toDto(staffEligibilityRepository.findBySchoolInfoAndSubject(schoolInfoId, masterSubject));
        } else {
            result =  staffEligibilityMapper.toDto(staffEligibilityRepository.findBySchoolInfoAndGrade(schoolInfoId, grade));
        }
        Collections.sort(result, new StaffEligibilityGradeAscComparator());
        return result;
    }

    @Override
    public List<StaffEligibilityDTO> getStaffEligibilitysByStaff(Long staffId, String subject, Grade grade) throws WitcurveException {
        MasterSubject masterSubject = null;
        List<StaffEligibilityDTO> result = new ArrayList<>();
        if (!Strings.isNullOrEmpty(subject)) {
            masterSubject = masterSubjectRepository.findByName(subject);
            if (masterSubject == null) {
                throw new WitcurveException("Invalid subject provided");
            }
        }
        if (masterSubject == null && grade == null) {
            result =  staffEligibilityMapper.toDto(staffEligibilityRepository.findByStaff(staffId));
        } else if (masterSubject != null && grade != null) {
            result =  staffEligibilityMapper.toDto(staffEligibilityRepository.findByStaffAndSubjectAndGrade(staffId, masterSubject, grade));
        } else if (masterSubject != null) {
            result =  staffEligibilityMapper.toDto(staffEligibilityRepository.findByStaffAndSubject(staffId, masterSubject));
        } else {
            result =  staffEligibilityMapper.toDto(staffEligibilityRepository.findByStaffAndGrade(staffId, grade));
        }
        Collections.sort(result, new StaffEligibilityGradeAscComparator());
        return result;
    }

    @Override
    public void deleteStaffEligibility(Long staffEligibilityId) throws WitcurveException {
        log.debug("Request to delete staff eligibility by id : {}", staffEligibilityId);
        Optional<StaffEligibility> staffEligibility = staffEligibilityRepository.findById(staffEligibilityId);
        if (!staffEligibility.isPresent()){
            throw new WitcurveException("No staffEligibility with given Id " + staffEligibilityId);
        } else {
            StaffEligibility se = staffEligibility.get();
            List<CourseTeacher> existingCourseTeachers = courseTeacherRepository.findByStaffAndSubjectAndGrade(se.getStaff().getId(),
                se.getMasterSubject(), se.getGrade());
            for(CourseTeacher courseTeacher : existingCourseTeachers) {
                if(courseTeacher.getActive()) {
                    if (existingCourseTeachers.size() > 0) {
                        throw new WitcurveException("This staff currently teaches the subject " + se.getMasterSubject().getName()
                            + " in grade " + se.getGrade()+", please remove them and try again");
                    }
                }
            }
        }
        staffEligibilityRepository.delete(staffEligibility.get());
    }

    public class StaffEligibilityGradeAscComparator implements Comparator<StaffEligibilityDTO> {
        @Override
        public int compare(StaffEligibilityDTO o1, StaffEligibilityDTO o2) {
            return o1.getGrade().compareTo(o2.getGrade());
        }
    }


}
