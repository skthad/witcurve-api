package com.witcurve.service;

import com.witcurve.service.dto.UserContextDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface UserContextService {

    UserContextDTO getCurrentUserContext() throws WitcurveException;
}
