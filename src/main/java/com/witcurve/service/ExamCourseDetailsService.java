package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.ExamCourseDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface ExamCourseDetailsService {

    List<ExamCourseDetailsDTO> saveOrUpdate(List<ExamCourseDetailsDTO> examCourseDetailsDTO, Long examId) throws WitcurveException;

    List<ExamCourseDetailsDTO> getExamCourseDetailsByGradeAndExamId(Grade grade, Long examId);

    List<ExamCourseDetailsDTO> getExamCourseDetailsForStudentOnDate(Long studentId, LocalDate date) throws WitcurveException;

    List<ExamCourseDetailsDTO> getExamCourseDetailsOnAGivenMonthForStudent(Long studentId, Integer month, Integer year) throws WitcurveException;

    List<ExamCourseDetailsDTO> getUpcomingExamCourseDetailsForStudent(Long studentId, LocalDate date) throws WitcurveException;

    List<ExamCourseDetailsDTO> getDairyCourseDetailsForStudent(Long studentId, LocalDate date) throws WitcurveException;

    List<ExamCourseDetailsDTO> getExamCourseDetailsForStaffOnDate(Long staffId, LocalDate date) throws WitcurveException;

    List<ExamCourseDetailsDTO> getExamCourseDetailsOnAGivenMonthForStaff(Long staffId, Integer month, Integer year) throws WitcurveException;

    List<ExamCourseDetailsDTO> getUpcomingExamCourseDetailsForStaff(Long staffId, LocalDate date) throws WitcurveException;

    List<ExamCourseDetailsDTO> getDiaryExamCourseDetailsForStaff(Long staffId, LocalDate date) throws WitcurveException;

    ExamCourseDetailsDTO getExamCourseDetailsById(Long examCourseDetailsId) throws WitcurveException;

    void deleteExamCourseDetails(Long examCourseDetailsId) throws WitcurveException;

}
