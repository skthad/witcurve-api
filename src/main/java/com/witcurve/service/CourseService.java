package com.witcurve.service;

import com.witcurve.service.dto.CourseDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface CourseService {

    CourseDTO saveOrUpdate(CourseDTO courseDTO);

    CourseDTO getCourseById(Long courseId) throws WitcurveException;
}

