package com.witcurve.service;

import com.witcurve.service.dto.CourseContentDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface CourseContentService {

    List<CourseContentDTO> saveOrUpdateForCourse(Long courseId, List<CourseContentDTO> courseContentDTOs) throws WitcurveException;

    CourseContentDTO updateSingleCourseContent(Long courseId, CourseContentDTO courseContentDTO) throws WitcurveException;

    CourseContentDTO getCourseContentById(Long courseContentId) throws WitcurveException;

    List<CourseContentDTO> getCourseContentsByCourseId(Long courseId, Boolean admin) throws WitcurveException;

    List<CourseContentDTO> getCourseContentsByEventId(Long eventId, Boolean forExam) throws WitcurveException;

    void deleteCourseContent(Long courseContentId) throws WitcurveException;

    void deleteCourseContentsByCourseId(Long courseId) throws WitcurveException;
}

