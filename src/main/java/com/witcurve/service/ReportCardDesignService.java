package com.witcurve.service;

import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.service.dto.ReportCardDesignDTO;

import java.util.List;

public interface ReportCardDesignService {

    List<ReportCardDesignDTO> saveOrUpdate(List<ReportCardDesignDTO> reportCardDesignDTOS, Long examId, String bindingId);

    List<ReportCardDesignDTO> findByExamIdOrBindingIdWithFieldType(Long examId, String bindingId, ReportFieldType fieldType);

    ReportCardDesignDTO findById(Long id);

    void deleteReportCardDesign(List<Long> ids);

}
