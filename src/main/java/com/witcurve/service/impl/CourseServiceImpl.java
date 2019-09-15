package com.witcurve.service.impl;

import com.witcurve.domain.Course;
import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.Standard;
import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ViewType;
import com.witcurve.repository.CourseRepository;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.repository.StandardRepository;
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

    @Override
    public CourseDTO saveOrUpdate(CourseDTO courseDTO) {
        log.debug("Request to save or update Course: {}", courseDTO);
        //todo Mandatory Course Created, create mandatory subject records with standardId and courseId.
        // Course is updated from mandatory to non mandatory - get list of student course ids for this course
        // and call deactivate service in student course service
        // non-mandatory to mandatory - create mandatory student course records for all students in this grade
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
        if(courseDTO.getCourseType().equals(CourseType.SCHOLASTIC)){
            if(courseDTO.getElective()&&courseDTO.getMandatory()||!courseDTO.getMandatory()&&!courseDTO.getElective()){
                throw new WitcurveException("Both elective and mandatory can't be true or false at same time");
            }
        }else if(courseDTO.getCourseType().equals(CourseType.NON_SCHOLASTIC)){
            courseDTO.setElective(false);
            courseDTO.setMandatory(false);
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
    public List<CourseDTO> getCourseBySchoolInfoAndGrade(Long schoolInfoId, Grade grade,CourseType courseType,Boolean elective,Boolean mandatory) throws WitcurveException {
        log.debug("Request to get courses in schoolInfo {} with grade : {}", schoolInfoId, grade);
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        List<Course>courses=null;
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No SchoolInfo with given id " + schoolInfoId);
        }
        if(courseType==null){
        courses = courseRepository.findBySchoolInfoAndGrade(schoolInfoId, grade);
        }else if(courseType!=null&&!elective&&!mandatory||courseType!=null&&elective==true&&mandatory==true){
            courses=courseRepository.findBySchoolInfoAndGradeAndCourseType(schoolInfoId,grade,courseType);
        }else if(courseType!=null&&elective==true&&mandatory==false){
            courses=courseRepository.findElectiveCourse(schoolInfoId,grade,courseType);
        }else if(courseType!=null&&elective==false&&mandatory==true){
            courses=courseRepository.findMandatoryCourse(schoolInfoId,grade,courseType);}
        return courseMapper.toDto(courses);
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
