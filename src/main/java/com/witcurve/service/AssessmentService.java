package com.witcurve.service;

import com.witcurve.service.dto.StudentMarksDTO;

import java.util.List;

public interface AssessmentService {

    List<StudentMarksDTO> enterStudentMarks(List<StudentMarksDTO> studentMarks);

    List<StudentMarksDTO> getStudentMarksByTestId(Long testId);

    List<StudentMarksDTO> getStudentMarksByStudentAndTestId(Long studentId, Long testId);
}
