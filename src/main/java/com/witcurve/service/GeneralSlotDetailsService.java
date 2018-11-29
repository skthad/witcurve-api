package com.witcurve.service;

import com.witcurve.domain.enumeration.GSDStatus;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface GeneralSlotDetailsService {

    List<GeneralSlotDetailsDTO> create(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs);

    List<GeneralSlotDetailsDTO> update(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs);

    void clone(Long sourceStandardId, List<Long> destinationStandardIds);

    void deactivate(List<Long> standardIds);

    void activate(Long standardId, String bindingId);

    GeneralSlotDetailsDTO getGeneralSlotDetailsById(Long generalSlotDetailsId) throws WitcurveException;

    List<GeneralSlotDetailsDTO> getGeneralSlotDetailsByStandardIdAndStatus(Long standardId, GSDStatus status) throws WitcurveException;

    void deleteByBindingId(String bindingId) throws WitcurveException;
}
