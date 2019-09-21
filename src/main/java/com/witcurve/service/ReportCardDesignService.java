package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.service.dto.ReportCardDesignDTO;

import java.util.List;

public interface ReportCardDesignService {

    List<ReportCardDesignDTO> saveOrUpdate(List<ReportCardDesignDTO> reportCardDesignDTOS, Long examId, Grade grade);

    List<ReportCardDesignDTO> findByExamIdOrBindingIdWithFieldType(Grade grade, Long examId, ReportFieldType fieldType);

    ReportCardDesignDTO findById(Long id);

    void deleteReportCardDesign(List<Long> ids);

}
