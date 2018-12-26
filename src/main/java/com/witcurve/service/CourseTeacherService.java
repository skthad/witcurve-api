package com.witcurve.service;

import com.witcurve.service.dto.CourseTeacherDTO;
import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface CourseTeacherService {

    CourseTeacherDTO saveOrUpdate(CourseTeacherDTO courseTeacherDTO);

    CourseTeacherDTO getCourseTeacherById(Long id) throws WitcurveException;

    void deleteCourseTeacher(Long courseTeacherId) throws WitcurveException;

    List<CourseTeacherDTO> getCourseTeachersByTeacherIdAndTermId(Long teacherId, Long termId) throws WitcurveException;

    List<CourseTeacherDTO> getCoursesByStandardId(Long standardId);

    List<CourseTeacherDTO> getCourseTeachersByStudentId(Long studentId) throws WitcurveException;
}
