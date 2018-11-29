package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.repository.SlotCourseDetailsRepository;
import com.witcurve.repository.StaffEligibilityRepository;
import com.witcurve.repository.StaffRepository;
import com.witcurve.service.GeneralSlotDetailsService;
import com.witcurve.service.SlotCourseDetailsService;
import com.witcurve.service.StaffService;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.service.mapper.StaffMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final Logger log = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StaffMapper staffMapper;

    @Autowired
    GeneralSlotDetailsService generalSlotDetailsService;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    SlotCourseDetailsService slotCourseDetailsService;

    @Autowired
    SlotCourseDetailsRepository slotCourseDetailsRepository;

    @Autowired
    StaffEligibilityRepository staffEligibilityRepository;

    @Override
    public StaffDTO saveOrUpdate(StaffDTO staffDTO) {
        log.debug("Request to save or update staff : {}", staffDTO);
        Staff staff = staffMapper.toEntity(staffDTO);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    @Override
    public StaffDTO getStaffById(Long staffId) throws WitcurveException {
        log.debug("Request to get staff with id : {}", staffId);
        Staff staff = staffRepository.findById(staffId).get();
        if (staff == null) {
            throw new WitcurveException("No staff exists with given id");
        }
        return staffMapper.toDto(staff);
    }

    @Override
    public void deleteStaffById(Long staffId) throws WitcurveException {
        log.debug("Request to delete staff with id : {}", staffId);
        Staff staff = staffRepository.findById(staffId).get();
        if (staff == null) {
            throw new WitcurveException("No staff exists with given id");
        }
        staffRepository.delete(staff);
    }

    @Override
    public List<StaffDTO> getSubstituteList(Long staffId, Long gsdId, LocalDate date) throws WitcurveException {

        SlotCourseDetails scd = slotCourseDetailsRepository.findByGsdAndDayOfWeek(gsdId, date.getDayOfWeek());

        if (!scd.getCourseTeacher().getTeacher().getId().equals(staffId)) {
            throw new WitcurveException("staffId provided is not matching with teacher in gsd id");
        }

        CourseTeacher courseTeacher = scd.getCourseTeacher();
        Standard standard = courseTeacher.getStandard();
        Grade grade = standard.getGrade();
        MasterSubject masterSubject = courseTeacher.getCourse().getMasterSubject();
        Long schoolId = scd.getCourseTeacher().getTeacher().getSchool().getId();
        // look for a staff who teaches given master subject in the given grade
        List<Staff> availableStaff = staffEligibilityRepository.findStaffByGradeAndSubject(
            grade, masterSubject, schoolId, staffId);

        if (availableStaff.size() == 0) {
            //look for a teacher who teaches any course in the given standard
            availableStaff = staffEligibilityRepository.findStaffByStandard(
                standard.getId(), schoolId, staffId);
        }
        if (availableStaff.size() == 0) {
            //look for a teacher who teaches any course in the given grade
            availableStaff = staffEligibilityRepository.findStaffByGrade(
                grade, schoolId, staffId);
        }

        if (availableStaff.size() == 0) {

            // look for a teacher who teaches a course with eligibleForSubstitute = true in the whole school
            availableStaff = courseTeacherRepository.findEligibleForSubstituteBySchoolId(
                staffId, schoolId);
        }

        return staffMapper.toDto(availableStaff);
    }
}
