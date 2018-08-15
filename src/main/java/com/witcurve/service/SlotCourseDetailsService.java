package com.witcurve.service;

import com.witcurve.domain.SlotCourseDetails;
import com.witcurve.service.dto.SlotCourseDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface SlotCourseDetailsService {

    SlotCourseDetailsDTO saveOrUpdate(SlotCourseDetailsDTO slotCourseDetailsDTO);

    SlotCourseDetailsDTO getSlotCourseDetailsById(Long slotCourseDetailsId) throws WitcurveException;

    void deleteSlotCourseDetailsById(Long slotCourseDetailsId) throws WitcurveException;
}
