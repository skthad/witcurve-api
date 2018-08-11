package com.witcurve.service;

import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface SchoolService {

    SchoolDTO saveOrUpdate(SchoolDTO schoolDTO);

    SchoolDTO getSchoolById(Long schoolId) throws WitcurveException;

}
