package com.witcurve.service;

import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface GeneralSlotDetailsService {

    GeneralSlotDetailsDTO saveOrUpdate(GeneralSlotDetailsDTO generalSlotDetailsDTO);

    GeneralSlotDetailsDTO getGeneralSlotDetailsById(Long generalSlotDetailsId) throws WitcurveException;

    void deleteGeneralSlotDetailsById(Long generalSlotDetailsId) throws WitcurveException;
}
