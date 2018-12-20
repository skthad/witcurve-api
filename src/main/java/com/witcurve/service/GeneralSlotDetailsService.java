package com.witcurve.service;

import com.witcurve.domain.enumeration.GSDStatus;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface GeneralSlotDetailsService {

    List<GeneralSlotDetailsDTO> create(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs, Boolean exam);

    List<GeneralSlotDetailsDTO> update(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs);

    void clone(Long sourceStandardId, List<Long> destinationStandardIds, Boolean exam);

    void deactivate(List<Long> standardIds, Boolean exam);

    void activate(Long standardId, String bindingId);

    GeneralSlotDetailsDTO getGeneralSlotDetailsById(Long generalSlotDetailsId) throws WitcurveException;

    List<GeneralSlotDetailsDTO> getGeneralSlotDetailsByStandardId(Long standardId, GSDStatus status, Long ExamId) throws WitcurveException;

    void deleteByBindingId(String bindingId) throws WitcurveException;
}
