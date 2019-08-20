package com.witcurve.service;

import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.domain.enumeration.ReportModelType;
import com.witcurve.service.dto.ReportCardDesignDTO;

import java.util.List;

public interface ReportCardDesignService {

    List<ReportCardDesignDTO> saveOrUpdate(List<ReportCardDesignDTO> reportCardDesignDTOS, Long schoolInfoId);

    List<ReportCardDesignDTO> findByModelTypeAndSchoolInfoId(ReportModelType modelType, Long schoolInfoId, ReportFieldType fieldType);

    List<ReportCardDesignDTO> findManualEntryFieldsByEcdIdOrEventId(ReportModelType modelType, Long id);

    void deleteReportCardDesign(List<Long> ids);

}
