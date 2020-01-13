package com.witcurve.service;

import com.witcurve.domain.ReportCardDesign;
import com.witcurve.service.dto.CourseGradeDTO;

import java.time.LocalDate;
import java.util.List;

public interface CourseGradeService {

    List<CourseGradeDTO> saveOrUpdateCourseGrade(List<CourseGradeDTO> courseGradeDTOs, Long rcdId, Long courseId);

    List<CourseGradeDTO> getCourseGradeByExamId(Long examId, Long courseId, Long rcdId, Long standardId);

    List<CourseGradeDTO> getAllGradesForAStudentInACourse(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate);

    List<CourseGradeDTO> getStudentMarksByRcdIdAndStudentId(ReportCardDesign reportCardDesign, Long studentId);

    void deleteStudentGrades(List<Long> studentGradeIds);
}
