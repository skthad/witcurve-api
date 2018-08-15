package com.witcurve.service.impl;

import com.witcurve.domain.Course;
import com.witcurve.repository.CourseRepository;
import com.witcurve.service.CourseService;
import com.witcurve.service.dto.CourseDTO;
import com.witcurve.service.mapper.CourseMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    private final Logger log  = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseMapper courseMapper;

    @Override
    public CourseDTO saveOrUpdate(CourseDTO courseDTO) {
        log.debug("Request to save or update Course: {}", courseDTO);
        Course course = courseMapper.toEntity(courseDTO);
        course = courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    @Override
    public CourseDTO getCourseById(Long courseId) throws WitcurveException {
        log.debug("Request to get course with id : {}", courseId);
        Course course = courseRepository.findById(courseId).get();

        if (course ==  null) {
            throw new WitcurveException("No Course with given id");
        }
        return courseMapper.toDto(course);
    }
}
