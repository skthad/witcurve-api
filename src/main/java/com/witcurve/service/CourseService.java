package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.CourseDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface CourseService {

    CourseDTO saveOrUpdate(CourseDTO courseDTO);

    CourseDTO getCourseById(Long courseId) throws WitcurveException;

    List<CourseDTO> getCourseBySchoolInfoAndGrade(Long schoolInfoId, Grade grade) throws WitcurveException;

    void deleteCourse(Long courseId) throws WitcurveException;
}

