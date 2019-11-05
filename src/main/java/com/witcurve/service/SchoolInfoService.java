package com.witcurve.service;

import com.witcurve.service.dto.SchoolInfoDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface SchoolInfoService {

    SchoolInfoDTO saveOrUpdate(SchoolInfoDTO schoolInfoDTO);

    SchoolInfoDTO getSchoolInfoById(Long schoolInfoId) throws WitcurveException;

    List<SchoolInfoDTO> getSchoolInfosBySchoolId(Long schoolId);

    void deleteSchoolInfo(Long schoolInfoId) throws WitcurveException;

    Long getSchoolInfoIdByUserId(Long userId);

    SchoolInfoDTO changeToPrimaryBoard(Long schoolInfoId);

}
