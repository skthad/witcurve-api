package com.witcurve.service.impl;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.service.CourseTeacherService;
import com.witcurve.service.dto.CourseTeacherDTO;
import com.witcurve.service.mapper.CourseTeacherMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourseTeacherServiceImpl implements CourseTeacherService {

    private final Logger log  = LoggerFactory.getLogger(CourseTeacherServiceImpl.class);

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    CourseTeacherMapper courseTeacherMapper;


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
}
