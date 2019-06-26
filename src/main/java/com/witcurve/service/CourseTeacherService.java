package com.witcurve.service;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.CourseTeacherDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.DayOfWeek;
import java.util.List;

public interface CourseTeacherService {

    CourseTeacherDTO saveOrUpdate(CourseTeacherDTO courseTeacherDTO) throws WitcurveException;

    CourseTeacherDTO getCourseTeacherById(Long id) throws WitcurveException;

    CourseTeacherDTO updateStatusForCourseTeacher(Long id, Boolean status) throws WitcurveException;

    List<CourseTeacherDTO> getCourseTeachersByTeacherId(Long teacherId);

    List<CourseTeacherDTO> getCoursesByStandardId(Long standardId, Boolean active);

    List<CourseTeacherDTO> getCourseTeachersByStudentId(Long studentId) throws WitcurveException;

    List<CourseTeacherDTO> getCourseTeacherSuggestionForSlot(Long gsdId, DayOfWeek dayOfWeek) throws WitcurveException;

    List<CourseTeacherDTO> getCourseTeachersByGradeAndSchoolInfoId(Grade grade, Long schoolInfoId, Boolean oneRecordForACourse);
}
