package com.witcurve.service.impl;

import com.witcurve.domain.Course;
import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.CourseRepository;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.service.CourseService;
import com.witcurve.service.dto.CourseDTO;
import com.witcurve.service.mapper.CourseMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    CourseTeacherRepository courseTeacherRepository;

    @Override
    public CourseDTO saveOrUpdate(CourseDTO courseDTO) {
        log.debug("Request to save or update Course: {}", courseDTO);
        if(courseDTO.getId() == null) {
            Course existingCourse = courseRepository.findBySchoolInfoAndGradeAndCourseCode(courseDTO.getSchoolInfoId(),
                courseDTO.getGrade(), courseDTO.getCourseCode());
            if(existingCourse != null) {
                if(existingCourse.getActive()) {
                    throw new WitcurveException("There already exists a subject code with given subject code details for this grade");
                }
                courseDTO.setId(existingCourse.getId());
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
        List<Course> courses = courseRepository.findBySchoolInfoAndGrade(schoolInfoId, grade);
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
}
