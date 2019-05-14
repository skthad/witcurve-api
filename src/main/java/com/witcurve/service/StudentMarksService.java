package com.witcurve.service;

import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface StudentMarksService {
    List<StudentMarksDTO> saveOrUpdateStudentMarks(List<StudentMarksDTO> studentMarksDTO, Long ecdId, Long eventId) throws WitcurveException;

    void deleteStudentMarks(Long studentMarksId) throws WitcurveException;

    List<StudentMarksDTO> getStudentMarksByEventId(Long eventId) throws WitcurveException;

    List<StudentMarksDTO> getStudentMarksByExamId(Long examId, Long ecdId, Long standardId, Boolean publishedOnly) throws WitcurveException;

    List<StudentMarksDTO> getAllMarksForAStudentInACourse(Long studentId, Long courseId, EventType type, LocalDate startDate, LocalDate endDate, Boolean publishedOnly) throws WitcurveException;

    List<StudentMarksDTO> getMarksForAllStudentsInAGradeAndCourse(Grade grade, Long courseId, EventType type, LocalDate startDate, LocalDate endDate, Boolean publishedOnly) throws WitcurveException;

    List<StudentMarksDTO> getMarksForAllStudentsInAStandardAndCourse(Long standardId, Long courseId, EventType type, LocalDate startDate, LocalDate endDate) throws WitcurveException;

}
