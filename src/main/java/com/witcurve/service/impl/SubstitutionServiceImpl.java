package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.*;
import com.witcurve.service.SubstitutionService;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.service.dto.SubstitutionDTO;
import com.witcurve.service.mapper.SlotCourseDetailsMapper;
import com.witcurve.service.mapper.StaffMapperLite;
import com.witcurve.service.mapper.SubstitutionMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    SlotCourseDetailsMapper slotCourseDetailsMapper;

    @Autowired
    SlotCourseDetailsRepository slotCourseDetailsRepository;

    @Autowired
    StaffEligibilityRepository staffEligibilityRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StaffMapperLite staffMapperLite;

    private List<Long> filterAvailableStaff(List<Long> availableStaff, SlotCourseDetails scd, Long schoolInfoId, LocalDate date, Long teacherId) {


        if (availableStaff.size() > 0) {

            Integer startTime = Integer.parseInt(scd.getGsd().getStart());
            Integer endTime = startTime + scd.getGsd().getDuration();

            List<Long> allocatedTeachers = slotCourseDetailsRepository
                .findAllocatedTeachersList(startTime, endTime, scd.getDayOfWeek(), schoolInfoId);
            allocatedTeachers.addAll(slotCourseDetailsRepository.findSubstitutedTeacherList(date, schoolInfoId, startTime, endTime));
            availableStaff.removeIf((Long a) -> (allocatedTeachers.indexOf(a) == teacherId || allocatedTeachers.indexOf(a) > -1));
            log.info("Final list : {}", availableStaff);
        }
        return availableStaff;
    }

    @Override
    public List<StaffDTO> getSubstituteSuggestion(Long scdId, LocalDate date) throws WitcurveException {

        Optional<SlotCourseDetails> result = slotCourseDetailsRepository.findById(scdId);

        if (!result.isPresent()) {
            throw new WitcurveException("No SCD found with id " + scdId);
        }

        SlotCourseDetails scd = result.get();
        CourseTeacher courseTeacher = scd.getCourseTeacher();
        Long teacherId = courseTeacher.getTeacher().getId();
        Standard standard = courseTeacher.getStandard();
        Grade grade = standard.getGrade();
        MasterSubject masterSubject = courseTeacher.getCourse().getMasterSubject();
        Long schoolInfoId = scd.getCourseTeacher().getTeacher().getSchoolInfo().getId();
        // look for a staff who teaches given master subject in the given grade
        List<Long> availableStaff = staffEligibilityRepository.findAvailableStaffInSchoolBySubjectAndGrade(
            schoolInfoId, masterSubject, grade, teacherId, date);
        availableStaff = filterAvailableStaff(availableStaff, scd, schoolInfoId, date, teacherId);

        log.info("Grade and Subject Teachers : {}",availableStaff);

        if (availableStaff.size() == 0) {
            //look for a teacher who teaches any course in the given standard
            availableStaff = courseTeacherRepository.findAvailableTeacherByStandardId(
                standard.getId(), teacherId, date);
            availableStaff = filterAvailableStaff(availableStaff, scd, schoolInfoId, date, teacherId);
            log.info("Standard Teachers : {}",availableStaff);
        }

        if (availableStaff.size() == 0) {

            // look for a teacher who teaches the given master subject in the whole school
            availableStaff = staffEligibilityRepository.findAvailableStaffInSchoolBySubject(
                schoolInfoId, masterSubject, teacherId, date);

            availableStaff = filterAvailableStaff(availableStaff, scd, schoolInfoId, date, teacherId);
            log.info("Subject Teachers : {}",availableStaff);

        }

        if (availableStaff.size() == 0) {
            //look for a teacher who teaches any course in the given grade
            availableStaff = staffEligibilityRepository.findAvailableStaffInSchoolByGrade(
                schoolInfoId, grade, teacherId, date);
            availableStaff = filterAvailableStaff(availableStaff, scd, schoolInfoId, date, teacherId);
            log.info("Grade Teachers : {}",availableStaff);
        }

        if (availableStaff.size() == 0) {

            // look for a teacher who teaches a course with eligibleForSubstitute = true in the whole school
            availableStaff = courseTeacherRepository.findEligibleForSubstituteBySchoolInfoId(
                teacherId, schoolInfoId);

            availableStaff = filterAvailableStaff(availableStaff, scd, schoolInfoId, date, teacherId);
            log.info("Teachers : {}",availableStaff);
        }
        List<Staff> staffList = staffRepository.findAllById(availableStaff);
        return staffMapperLite.toDto(staffList);
    }

    @Override
    public List<SubstitutionDTO> getSubstitutions(List<Long> gsdIds, LocalDate date) {
        List<Substitution> substitutions = substitutionRepository.findByDateAndGSDs(date, gsdIds);
        return substitutionMapper.toDto(substitutions);
    }

    public SubstitutionDTO substitute(SubstitutionDTO substitutionDTO) {
        Optional<Staff> teacher = staffRepository.findById(substitutionDTO.getTeacher().getId());
        if(!teacher.isPresent()) {
            throw new WitcurveException("There is no staff with given id "+substitutionDTO.getTeacher().getId());
        }
        Optional<SlotCourseDetails> slotCourseDetails = slotCourseDetailsRepository.findById(substitutionDTO.getScd().getId());
        if(!slotCourseDetails.isPresent()) {
            throw new WitcurveException("There is no slot course details with given id "+substitutionDTO.getTeacher().getId());
        }
        if(!slotCourseDetails.get().getDayOfWeek().equals(substitutionDTO.getDate().getDayOfWeek())) {
            throw new WitcurveException("Weekday of slot and date doesn't match");
        }
        Substitution substitution = substitutionRepository.save(substitutionMapper.toEntity(substitutionDTO));
        SubstitutionDTO result = substitutionMapper.toDto(substitution);
        result.getTeacher().setFirstName(teacher.get().getFirstName());
        result.getTeacher().setLastName(teacher.get().getLastName());
        return result;
    }

}
