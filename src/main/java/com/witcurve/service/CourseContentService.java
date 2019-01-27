package com.witcurve.service;

import com.witcurve.service.dto.CourseContentDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface CourseContentService {

    List<CourseContentDTO> saveOrUpdate(List<CourseContentDTO> courseContentDTOs);

    CourseContentDTO getCourseContentById(Long courseContentId) throws WitcurveException;

    List<CourseContentDTO> getCourseContentsByCourseId(Long courseId) throws WitcurveException;

    void deleteCourseContent(Long courseContentId) throws WitcurveException;
}

