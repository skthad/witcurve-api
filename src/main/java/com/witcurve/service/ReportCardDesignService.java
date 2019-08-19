package com.witcurve.service;

import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.domain.enumeration.ReportModelType;
import com.witcurve.service.dto.ReportCardDesignDTO;

import java.util.List;

public interface ReportCardDesignService {

    List<ReportCardDesignDTO> saveOrUpdate(List<ReportCardDesignDTO> reportCardDesignDTOS);

    List<ReportCardDesignDTO> findByModelTypeAndSchoolInfoId(ReportModelType modelType, Long schoolInfoId, ReportFieldType fieldType);

}
