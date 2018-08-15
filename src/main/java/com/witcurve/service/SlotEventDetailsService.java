package com.witcurve.service;

import com.witcurve.service.dto.SlotEventDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface SlotEventDetailsService {

    SlotEventDetailsDTO saveOrUpdate(SlotEventDetailsDTO slotEventDetailsDTO);

    SlotEventDetailsDTO getSlotEventDetailsById(Long slotEventDetailsId) throws WitcurveException;

    void deleteSlotEventDetailsById(Long slotEventDetailsId) throws WitcurveException;
}
