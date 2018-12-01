package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.*;
import com.witcurve.service.SubstitutionService;
import com.witcurve.service.dto.SlotCourseDetailsDTO;
import com.witcurve.service.dto.SubstitutionDTO;
import com.witcurve.service.mapper.SlotCourseDetailsMapper;
import com.witcurve.service.mapper.StaffMapper;
import com.witcurve.service.mapper.SubstitutionMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class SubstitutionServiceImpl implements SubstitutionService {

    private final Logger log  = LoggerFactory.getLogger(SubstitutionServiceImpl.class);

    @Autowired
    SubstitutionMapper substitutionMapper;

    @Autowired
    SubstitutionRepository substitutionRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    SlotCourseDetailsMapper slotCourseDetailsMapper;

    @Autowired
    SlotCourseDetailsRepository slotCourseDetailsRepository;

    @Autowired
    StaffEligibilityRepository staffEligibilityRepository;

    @Autowired
    StaffMapper staffMapper;

    @Override
    public HashMap<Long, List<SlotCourseDetailsDTO>> getSubstituteSuggestion(Long gsdId, Long teacherId, LocalDate date) throws WitcurveException {

        HashMap<Long, List<SlotCourseDetailsDTO>> staffSCDMap = new HashMap<>();
        SlotCourseDetails scd = slotCourseDetailsRepository.findByGsdAndDayOfWeek(gsdId, date.getDayOfWeek());

        if (!scd.getCourseTeacher().getTeacher().getId().equals(teacherId)) {
            throw new WitcurveException("staffId provided is not matching with teacher in gsd id");
        }

        CourseTeacher courseTeacher = scd.getCourseTeacher();
        Standard standard = courseTeacher.getStandard();
        Grade grade = standard.getGrade();
        MasterSubject masterSubject = courseTeacher.getCourse().getMasterSubject();
        Long schoolId = scd.getCourseTeacher().getTeacher().getSchool().getId();
        // look for a staff who teaches given master subject in the given grade
        List<Long> availableStaff = staffEligibilityRepository.findStaffByGradeAndSubject(
            grade, masterSubject, schoolId, teacherId);

        if (availableStaff.size() == 0) {
            //look for a teacher who teaches any course in the given standard
            availableStaff = staffEligibilityRepository.findStaffByStandard(
                standard.getId(), schoolId, teacherId);
        }
        if (availableStaff.size() == 0) {
            //look for a teacher who teaches any course in the given grade
            availableStaff = staffEligibilityRepository.findStaffByGrade(
                grade, schoolId, teacherId);
        }

        if (availableStaff.size() == 0) {

            // look for a teacher who teaches a course with eligibleForSubstitute = true in the whole school
            availableStaff = courseTeacherRepository.findEligibleForSubstituteBySchoolId(
                teacherId, schoolId);
        }

        // should not be in substitution table already
        if (availableStaff.size() > 0) {
            List<Long> alreadySubstituted = substitutionRepository.alreadySubstitutedTeacherList(gsdId, availableStaff, date);
            availableStaff.removeIf((Long a) -> alreadySubstituted.indexOf(a) > -1);
        }

        // should not be absent
        if (availableStaff.size() > 0) {
            List<Long> absentTeachers = eventRepository.findAbsentTeacherList(availableStaff, date);
            availableStaff.removeIf((Long a) -> absentTeachers.indexOf(a) > -1);
        }

        if (availableStaff.size() > 0) {
            List<SlotCourseDetails> allocatedCourses = slotCourseDetailsRepository.allocatedCourses(availableStaff, date.getDayOfWeek());
            for (SlotCourseDetails allocatedScd : allocatedCourses) {
                if (staffSCDMap.get(allocatedScd.getCourseTeacher().getTeacher().getId()) == null) {
                    staffSCDMap.put(allocatedScd.getCourseTeacher().getTeacher().getId(),new ArrayList<>());
                }
                staffSCDMap.get(allocatedScd.getCourseTeacher().getTeacher().getId()).add(slotCourseDetailsMapper.toDto(allocatedScd));
            }
        }

        // should not be in some other scd during same time
        /*if (availableStaff.size() > 0) {
            List<Long> allocatedTeachers = slotCourseDetailsRepository.allocatedTeacherList(
                scd.getGsd().getStart(), availableStaff, date.getDayOfWeek());
            availableStaff.removeIf((Long a) -> allocatedTeachers.indexOf(a) > -1);
        }*/
        return staffSCDMap;
    }

    public SubstitutionDTO substitute(SubstitutionDTO substitutionDTO) throws WitcurveException {
        Substitution substitution = substitutionRepository.save(substitutionMapper.toEntity(substitutionDTO));
        return substitutionMapper.toDto(substitution);
    }

}
