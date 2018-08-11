package com.witcurve.service;

import com.witcurve.service.dto.GuardianDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface GuardianService {

    GuardianDTO saveOrUpdate(GuardianDTO guardianDTO);

    GuardianDTO getGuardianById(Long guardianId) throws WitcurveException;

    void deleteGuardianById(Long guardianId) throws WitcurveException;
}
