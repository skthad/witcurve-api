package com.witcurve.service;

import com.witcurve.service.dto.CourseTeacherDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface CourseTeacherService {

    CourseTeacherDTO saveOrUpdate(CourseTeacherDTO courseTeacherDTO);

    CourseTeacherDTO getCourseTeacherById(Long id) throws WitcurveException;

    void deleteCourseTeacher(Long courseTeacherId) throws WitcurveException;
}
