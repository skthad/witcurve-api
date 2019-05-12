package com.witcurve.service;

import com.witcurve.service.dto.MobileMetaDataDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface MobileMetaDataService {

    MobileMetaDataDTO saveOrUpdate(MobileMetaDataDTO mobileMetaDataDTO);

    List<MobileMetaDataDTO> getMobileMetaDataByInstitute(Long instituteId) throws WitcurveException;

    MobileMetaDataDTO getMobileMetaDataById(Long mobileMetaDataId) throws WitcurveException;

    void deleteMobileMetaDataByInstituteId(Long instituteId) throws WitcurveException;
}
