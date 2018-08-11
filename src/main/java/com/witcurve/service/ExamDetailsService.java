package com.witcurve.service;

import com.witcurve.service.dto.ExamDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface ExamDetailsService {

    ExamDetailsDTO saveOrUpdate(ExamDetailsDTO examDetailsDTO);

    ExamDetailsDTO getExamDetailsById(Long examDetailsId) throws WitcurveException;

    void deleteExamDetails(Long examDetailsId) throws WitcurveException;
}
