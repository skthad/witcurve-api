package com.witcurve.service;

import com.witcurve.service.dto.ExamDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface ExamService {

    ExamDTO saveOrUpdate(ExamDTO examDTO);

    ExamDTO getExamById(Long examId) throws WitcurveException;

    void deleteExam(Long examId) throws WitcurveException;
}
