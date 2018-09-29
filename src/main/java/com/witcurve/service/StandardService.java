package com.witcurve.service;

import com.witcurve.service.dto.StandardDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface StandardService {

    StandardDTO saveOrUpdateStandard(StandardDTO standardDTO);

    StandardDTO getStandardById(Long id) throws WitcurveException;

    void deleteStandard(Long standardId) throws WitcurveException;


}
