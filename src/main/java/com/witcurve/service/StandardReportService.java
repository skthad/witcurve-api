package com.witcurve.service;

import com.witcurve.service.impl.StandardReportDTO;

import java.util.List;

public interface StandardReportService {

    StandardReportDTO saveOrUpdate(StandardReportDTO standardReportDTO);

    List<StandardReportDTO> findByExamId(Long examId);

    StandardReportDTO findById(Long id);

    StandardReportDTO generateStandardReport(Long standardId, Long reportCardId);

    void createReportCards(StandardReportDTO standardReportDTO);



}
