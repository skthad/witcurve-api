package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.repository.*;
import com.witcurve.service.CourseTeacherService;
import com.witcurve.service.dto.CourseTeacherDTO;
import com.witcurve.service.mapper.CourseTeacherMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseTeacherServiceImpl implements CourseTeacherService {

    private final Logger log = LoggerFactory.getLogger(CourseTeacherServiceImpl.class);

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    StaffEligibilityRepository staffEligibilityRepository;

    @Autowired
    SlotCourseDetailsRepository slotCourseDetailsRepository;

    @Autowired
    GeneralSlotDetailsRepository generalSlotDetailsRepository;

    @Override
    public List<CourseTeacherDTO> saveOrUpdate(List<CourseTeacherDTO> courseTeacherDTOs) throws WitcurveException {
        log.debug("Request to save or update CourseTeachers", courseTeacherDTOs);
        List<CourseTeacher> result = new ArrayList<>();
        for(CourseTeacherDTO courseTeacherDTO : courseTeacherDTOs) {
            Optional<Course> course = courseRepository.findById(courseTeacherDTO.getCourse().getId());
            if (!course.isPresent()) {
                throw new WitcurveException("A valid course id must be provided");
            }
            Optional<Standard> standard = standardRepository.findById(courseTeacherDTO.getStandard().getId());

            if (!standard.isPresent()) {
                throw new WitcurveException("a valid standard id must be provided");
            }
            if (Boolean.TRUE.equals(courseTeacherDTO.getActive())) {

                List<StaffEligibility> se = staffEligibilityRepository
                    .findByStaffAndSubjectAndGrade(courseTeacherDTO.getTeacher().getId(),
                        course.get().getMasterSubject(), standard.get().getGrade());
                if (se.size() == 0) {
                    throw new WitcurveException("Staff does not meet the eligibility criteria");
                }

            }
            CourseTeacher courseTeacher = courseTeacherMapper.toEntity(courseTeacherDTO);
            courseTeacher = courseTeacherRepository.save(courseTeacher);
            result.add(courseTeacher);
        }
        return courseTeacherMapper.toDto(result);
    }


    @Override
    public CourseTeacherDTO getCourseTeacherById(Long id) throws WitcurveException {
        log.debug("Request to get course teacher by id : {}", id);
        Optional<CourseTeacher> courseTeacher = courseTeacherRepository.findById(id);
        if (!courseTeacher.isPresent()) {
            throw new WitcurveException("No course tecaher with given id " + id);
        }
        return courseTeacherMapper.toDto(courseTeacher.get());
    }

    @Override
    public List<CourseTeacherDTO> getCourseTeachersByTeacherId(Long teacherId) {
        log.debug("Request to get all course teachers by teacher id : {}", teacherId);

        List<CourseTeacher> result = courseTeacherRepository.findByTeacherId(teacherId);
        return courseTeacherMapper.toDto(result);
    }

    @Override
    public List<CourseTeacherDTO> getCoursesByStandardId(Long standardId) {
        List<CourseTeacher> results = courseTeacherRepository.findByStandardId(standardId);

        return courseTeacherMapper.toDto(results);
    }

    @Override
    public List<CourseTeacherDTO> getCourseTeachersByCourseAndStandardId(Long courseId, Long standardId) throws WitcurveException {
        log.debug("Request to get list of all course teachers by standard id : {}",standardId +" and course id : {}", courseId);
        List<CourseTeacher> result = courseTeacherRepository.findByCourseAndStandardId(courseId,standardId);
        return courseTeacherMapper.toDto(result);
    }

    @Override
    public List<CourseTeacherDTO> getCourseTeachersByStudentId(Long studentId) throws WitcurveException{
        Long std = studentStandardRepository.getStandardIdByStudentId(studentId);
            List<CourseTeacher> result = courseTeacherRepository.findByStandardId(std);
        if(studentStandardRepository.getByStudentId(studentId).size()>1) {
              throw new WitcurveException("standard repository is giving more than one rows at a time.");
        }
        return courseTeacherMapper.toDto(result);
    }

    @Override
    public List<CourseTeacherDTO> getCourseTeacherSuggestionForSlot(Long gsdId, DayOfWeek dayOfWeek) throws WitcurveException {

        Optional<GeneralSlotDetails> gsd = generalSlotDetailsRepository.findById(gsdId);
        if (!gsd.isPresent()) {
            throw new WitcurveException("No gsd with given id " + gsdId);
        }
        Integer startTime = Integer.parseInt(gsd.get().getStart());
        Integer endTime = startTime + gsd.get().getDuration();

        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByStandardId(gsd.get().getStandard().getId());
        List<Long> allocatedTeachers = slotCourseDetailsRepository.findAllocatedTeachersList(startTime, endTime, dayOfWeek, gsd.get().getStandard().getSchoolInfo().getId());

        courseTeachers.removeIf((CourseTeacher ct) -> allocatedTeachers.indexOf(ct.getTeacher().getId()) > -1);

        return courseTeacherMapper.toDto(courseTeachers);
    }

    @Override
    public void deleteCourseTeacher(Long courseTeacherId) throws WitcurveException {
        log.debug("Request to delete course teacher by id : {}", courseTeacherId);
        Optional<CourseTeacher> courseTeacher = courseTeacherRepository.findById(courseTeacherId);
        if (!courseTeacher.isPresent()) {
            throw new WitcurveException("No course teacher with given id " + courseTeacherId);
        }
        courseTeacherRepository.delete(courseTeacher.get());
    }
}
