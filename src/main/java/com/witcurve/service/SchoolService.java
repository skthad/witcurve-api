package com.witcurve.service;

import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.service.dto.SchoolInfoDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface SchoolService {

    SchoolDTO saveOrUpdate(SchoolDTO schoolDTO);

    SchoolDTO getSchoolById(Long schoolId) throws WitcurveException;

    void deleteSchool(Long schoolId) throws WitcurveException;

    SchoolInfoDTO updateSchoolInfo(Long schoolId, String board, String medium) throws WitcurveException;
}
