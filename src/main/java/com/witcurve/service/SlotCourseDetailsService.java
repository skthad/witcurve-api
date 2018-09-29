package com.witcurve.service;

import com.witcurve.service.dto.SlotCourseDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface SlotCourseDetailsService {

    SlotCourseDetailsDTO saveOrUpdate(SlotCourseDetailsDTO slotCourseDetailsDTO);

    SlotCourseDetailsDTO getSlotCourseDetailsById(Long slotCourseDetailsId) throws WitcurveException;

    void deleteSlotCourseDetails(Long slotCourseDetailsId) throws WitcurveException;

    List<SlotCourseDetailsDTO> getSlotCourseDetailsByStandardId(Long standardId);

    List<SlotCourseDetailsDTO> getSlotCourseDetailsByTeacherIdAndTermId(Long teacherId, Long termId);
}
