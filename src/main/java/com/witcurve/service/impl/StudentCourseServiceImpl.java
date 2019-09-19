package com.witcurve.service.impl;

import com.witcurve.domain.StudentCourse;
import com.witcurve.repository.StudentCourseRepository;
import com.witcurve.service.StudentCourseService;
import com.witcurve.service.dto.StudentCourseDTO;
import com.witcurve.service.mapper.StudentCourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StudentCourseServiceImpl implements StudentCourseService {

    private final Logger log = LoggerFactory.getLogger(StudentCourseServiceImpl.class);

    @Autowired
    StudentCourseRepository studentCourseRepository;

    @Autowired
    StudentCourseMapper studentCourseMapper;

    @Override
    public List<StudentCourseDTO> saveOrUpdate(List<StudentCourseDTO> studentCourseDTOs) {
        log.debug("Request to save studentCourses : {}", studentCourseDTOs);
        List<StudentCourse> result = new ArrayList<>();
        for(StudentCourseDTO studentCourseDTO : studentCourseDTOs) {
            // no need for student course id
            studentCourseDTO.setId(null);
            StudentCourse studentCourse = studentCourseRepository.
                getStudentCourseByStudentStandardIdAndCourseId(studentCourseDTO.getStudentStandardId(), studentCourseDTO.getCourseId());
            if(studentCourse == null) {
                studentCourse = studentCourseMapper.toEntity(studentCourseDTO);
                studentCourse.setActive(true);
                studentCourse = studentCourseRepository.save(studentCourse);
            } else {
                studentCourse.setActive(true);
            }
            result.add(studentCourse);
        }
        return studentCourseMapper.toDto(result);

    }

    @Override
    public List<StudentCourseDTO> getSelectiveCoursesByStudent(Long studentId) {
        log.debug("Get list of scholastic elective and non-scholastic studentCourses by student with id : {}", studentId);
        List<StudentCourse> studentCourses = studentCourseRepository.getStudentCoursesByStudentId(studentId);
        return studentCourseMapper.toDto(studentCourses);
    }

    @Override
    public List<StudentCourseDTO> getSelectiveCoursesByStandard(Long standardId) {
        log.debug("Get list of scholastic elective and non-scholastic studentCourses by standard with id : {}", standardId);
        List<StudentCourse> studentCourses = studentCourseRepository.getStudentCoursesByStandardId(standardId);
        return studentCourseMapper.toDto(studentCourses);
    }

    @Override
    public void deactivateStudentCourseByIds(List<Long> ids) {
        log.debug("Deactivate student courses with ids : {}", ids);
        studentCourseRepository.deactivateStudentCourseByIds(ids);
    }




}
