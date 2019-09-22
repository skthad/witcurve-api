package com.witcurve.service;

import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface StudentMarksService {
    List<StudentMarksDTO> saveOrUpdateStudentMarks(List<StudentMarksDTO> studentMarksDTO, Long eventId, Long rcdId, Long courseId) throws WitcurveException;

    void deleteStudentMarks(Long studentMarksId) throws WitcurveException;

    List<StudentMarksDTO> getStudentMarksByEventId(Long eventId, Long rcdId) throws WitcurveException;

    List<StudentMarksDTO> getStudentMarksByExamId(Long examId, Long courseId, Long rcdId, Long standardId) throws WitcurveException;

    List<StudentMarksDTO> getAllMarksForAStudentInACourse(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate) throws WitcurveException;

}
