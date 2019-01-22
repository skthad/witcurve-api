package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.ExamCourseDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface ExamCourseDetailsService {

    ExamCourseDetailsDTO saveOrUpdate(ExamCourseDetailsDTO examCourseDetailsDTO);

    ExamCourseDetailsDTO getExamCourseDetailsById(Long examCourseDetailsId) throws WitcurveException;

    void deleteExamCourseDetails(Long examCourseDetailsId) throws WitcurveException;

    List<ExamCourseDetailsDTO> getExamCourseDetailsByGradeAndExamId(Grade grade, Long examId);

    List<ExamCourseDetailsDTO> getExamCourseDetailsByTeacherIdAndExamId(Long teacherId, Long examId);
}
