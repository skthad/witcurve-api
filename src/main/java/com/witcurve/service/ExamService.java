package com.witcurve.service;

import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.ExamDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ExamService {

    ExamDTO saveOrUpdate(ExamDTO examDTO) throws WitcurveException;

    ExamDTO updateExamStatus(Long examId, ExamStatus status) throws WitcurveException;

    ExamDTO getExamById(Long examId) throws WitcurveException;

    Page<ExamDTO> getExamsBetweenDates(Long schoolInfoId, LocalDate fromDate, LocalDate endDate, Grade grade, List<ExamStatus> statusList, Pageable pageable) throws WitcurveException;

    void deleteExam(Long examId) throws WitcurveException;
}
