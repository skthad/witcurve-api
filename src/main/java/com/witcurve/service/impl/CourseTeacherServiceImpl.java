package com.witcurve.service.impl;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.StudentStandard;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.service.CourseTeacherService;
import com.witcurve.service.StudentStandardService;
import com.witcurve.service.dto.CourseTeacherDTO;
import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.service.mapper.CourseTeacherMapper;
import com.witcurve.service.mapper.StudentStandardMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    StudentStandardMapper studentStandardMapper;


    @Override
    public CourseTeacherDTO saveOrUpdate(CourseTeacherDTO courseTeacherDTO) {
        log.debug("Request to save or update CourseTeacher", courseTeacherDTO);

        CourseTeacher courseTeacher = courseTeacherMapper.toEntity(courseTeacherDTO);
        courseTeacher = courseTeacherRepository.save(courseTeacher);

        return courseTeacherMapper.toDto(courseTeacher);
    }


    @Override
    public CourseTeacherDTO getCourseTeacherById(Long id) throws WitcurveException {
        log.debug("Request to get course teacher by id : {}", id);
        CourseTeacher courseTeacher = courseTeacherRepository.findById(id).get();
        if (courseTeacher == null) {
            throw new WitcurveException("No course tecaher with given id");
        }
        return courseTeacherMapper.toDto(courseTeacher);
    }

    @Override
    public void deleteCourseTeacher(Long courseTeacherId) throws WitcurveException {
        log.debug("Request to delete course teacher by id : {}", courseTeacherId);
        CourseTeacher courseTeacher = courseTeacherRepository.findById(courseTeacherId).get();
        if (courseTeacher == null) {
            throw new WitcurveException("No course tecaher with given id");
        }
        courseTeacherRepository.delete(courseTeacher);
    }

    @Override
    public List<CourseTeacherDTO> getCourseTeachersByTeacherIdAndTermId(Long teacherId, Long termId) throws WitcurveException {
        log.debug("Request to get all course teachers by teacher id : {}", teacherId);
        List<CourseTeacher> result;
        if (termId == null) {
            result = courseTeacherRepository.findByTeacherId(teacherId);
        } else {
            result = courseTeacherRepository.findByTeacherIdAndTermId(teacherId, termId);
        }
        return courseTeacherMapper.toDto(result);
    }

    @Override
    public List<CourseTeacherDTO> getCoursesByStandardId(Long standardId) {
        List<CourseTeacher> results = courseTeacherRepository.findByStandardId(standardId);

        return courseTeacherMapper.toDto(results);
    }

    @Override
    public List<CourseTeacherDTO> getCourseTeachersByStudentId(Long studentId) throws WitcurveException{
        Long std = studentStandardRepository.getStandardIdByStudentId(studentId);
            List<CourseTeacher> result = courseTeacherRepository.findByStandardId(std);
        if(studentStandardRepository.getByStudentId(studentId).size()>1) {
              throw new WitcurveException("standard repository is giving more than one rows at a time !!");
        }
        return courseTeacherMapper.toDto(result);
    }
}
