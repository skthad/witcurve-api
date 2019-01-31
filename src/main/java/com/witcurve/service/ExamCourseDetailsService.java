package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.ExamCourseDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface ExamCourseDetailsService {

    List<ExamCourseDetailsDTO> saveOrUpdate(List<ExamCourseDetailsDTO> examCourseDetailsDTO, Long examId) throws WitcurveException;

    List<ExamCourseDetailsDTO> getExamCourseDetailsByGradeAndExamId(Grade grade, Long examId);

    ExamCourseDetailsDTO getExamCourseDetailsById(Long examCourseDetailsId) throws WitcurveException;

    void deleteExamCourseDetails(Long examCourseDetailsId) throws WitcurveException;

}
