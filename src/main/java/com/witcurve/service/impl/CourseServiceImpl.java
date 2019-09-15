package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ViewType;
import com.witcurve.repository.*;
import com.witcurve.service.AcademicSessionService;
import com.witcurve.service.CourseContentService;
import com.witcurve.service.CourseService;
import com.witcurve.service.EventService;
import com.witcurve.service.dto.*;
import com.witcurve.service.mapper.CourseMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final Logger log  = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseContentService courseContentService;

    @Autowired
    EventService eventService;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    AcademicSessionService academicSessionService;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Override
    public CourseDTO saveOrUpdate(CourseDTO courseDTO) {
        log.debug("Request to save or update Course: {}", courseDTO);
        List<Course> existingCourses = courseRepository.findBySchoolInfoAndGradeAndCourseCode(courseDTO.getSchoolInfoId(),
            courseDTO.getGrade(), courseDTO.getCourseCode());
        for(Course existingCourse : existingCourses) {
            if(existingCourse != null) {
                if(courseDTO.getId() == null) {
                    if(existingCourse.getActive()) {
                        throw new WitcurveException("There already exists a subject code with given subject code details for this grade");
                    }
                } else {
                    if(existingCourse.getActive() && !existingCourse.getId().equals(courseDTO.getId())) {
                        throw new WitcurveException("There already exists a subject code with given subject code details for this grade");
                    }
                }
            }
        }
        Course course = courseMapper.toEntity(courseDTO);
        course = courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    @Override
    public CourseDTO getCourseById(Long courseId) throws WitcurveException {
        log.debug("Request to get course with id : {}", courseId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        return courseMapper.toDto(course.get());
    }



    @Override
    public List<CourseDTO> getCourseBySchoolInfoAndGrade(Long schoolInfoId, Grade grade) throws WitcurveException {
        log.debug("Request to get courses in schoolInfo {} with grade : {}", schoolInfoId, grade);
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No SchoolInfo with given id " + schoolInfoId);
        }
        List<Course> courses = courseRepository.findBySchoolInfoAndGrade(schoolInfoId, grade);
        return courseMapper.toDto(courses);
    }

    @Override
    public List<CourseDTO>  getCourseByStudentId(Long studentId) {
        log.debug("Request to get Courses for student with id : {}", studentId);
        //todo get course from student courses later onwards
        List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(studentId);
        return getCourseBySchoolInfoAndGrade(studentStandards.get(0).getStandard().getSchoolInfo().getId(), studentStandards.get(0).getStandard().getGrade());
    }

    @Override
    public void deleteCourse(Long courseId) throws WitcurveException {
        log.debug("Request to delete course with id {}", courseId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("No Course with given id " + courseId);
        }
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByCourseId(courseId);
        if(courseTeachers.size() !=0) {
            throw new WitcurveException("There are some faculty assigned to this course, please deactivate them and try again");
        }
        course.get().setActive(false);
    }

    @Override
    public CourseTrackDTO getCourseTrackById(Long courseId, Long staffId) throws WitcurveException {
        CourseDTO courseDTO = getCourseById(courseId);
        List<CourseContentDTO> courseContentDTOS = courseContentService.getCourseContentsByCourseId(courseId, Boolean.TRUE);

        AcademicSessionDTO currentSession = academicSessionService.getCurrentSessionByDate(courseDTO.getSchoolInfoId(), LocalDate.now());
        List<Standard> standards = standardRepository.findByGradeAndSchoolInfoId(courseDTO.getGrade(), courseDTO.getSchoolInfoId());

        Map<String, Map<String, Long>> sectionTopicCountMap = new HashMap<>();
        for (Standard standard: standards) {
            sectionTopicCountMap.computeIfAbsent(standard.getSection(), k -> new HashMap<>());
            Map<String, Long> topicCountMap = sectionTopicCountMap.get(standard.getSection());

            List<EventDTO> dailyUpdates = eventService.findAllTestAndAssignmentAndDailyUpdateByStandardAndCourse(Pageable.unpaged(), currentSession.getStartDate(),
                currentSession.getStartDate().plusYears(1).minusDays(1), ViewType.DAILY_UPDATE, standard.getId(), courseDTO.getId(), staffId).getContent();

            for (CourseContentDTO courseContentDTO : courseContentDTOS) {
                Long count = dailyUpdates.stream()
                    .filter(eventDTO -> !CollectionUtils.isEmpty(eventDTO.getCourseContentIds()) && eventDTO.getCourseContentIds().contains(courseContentDTO.getId()))
                    .count();
                topicCountMap.put(courseContentDTO.getIndex(), count);
            }
        }

        return new CourseTrackDTO(courseDTO, courseContentDTOS, sectionTopicCountMap);
    }
}
