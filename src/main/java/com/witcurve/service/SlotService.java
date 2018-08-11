package com.witcurve.service;

import com.witcurve.service.dto.SlotDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface SlotService {

    SlotDTO saveOrUpdate(SlotDTO slotDTO);

    SlotDTO getSlotById(Long slotId) throws WitcurveException;

    void deleteSlotById(Long slotId) throws WitcurveException;
}
