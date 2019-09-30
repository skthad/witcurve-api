package com.witcurve.service;

import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.CourseDTO;
import com.witcurve.service.dto.CourseTrackDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface CourseService {

    CourseDTO saveOrUpdate(CourseDTO courseDTO);

    CourseDTO getCourseById(Long courseId) throws WitcurveException;

    CourseTrackDTO getCourseTrackById(Long courseId, Long staffId) throws WitcurveException;

    List<CourseDTO> getCourseBySchoolInfoAndGrade(Long schoolInfoId, Grade grade, CourseType courseType,Boolean elective,Boolean mandatory) throws WitcurveException;

    List<CourseDTO> getCourseByExamIdAndGradeAndCourseType(Long examId, Grade grade, CourseType courseType);

    void deleteCourse(Long courseId) throws WitcurveException;

    List<CourseDTO> getCourseByStudentId(Long studentId) throws WitcurveException;

}

