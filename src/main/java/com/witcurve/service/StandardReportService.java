package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.impl.StandardReportDTO;

import java.util.List;

public interface StandardReportService {

    StandardReportDTO saveOrUpdate(StandardReportDTO standardReportDTO);

    List<StandardReportDTO> findByExamId(Long examId, Grade grade);

    StandardReportDTO findById(Long id);

    StandardReportDTO generateStandardReport(Long standardId, Long reportCardId);

    void createReportCards(StandardReportDTO standardReportDTO, List<Long> disableStudentIds);

}
