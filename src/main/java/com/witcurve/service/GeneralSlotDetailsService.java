package com.witcurve.service;

import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface GeneralSlotDetailsService {

    List<GeneralSlotDetailsDTO> saveOrUpdate(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs);

    void clone(Long sourceStandardId, List<Long> destinationStandardIds);

    GeneralSlotDetailsDTO getGeneralSlotDetailsById(Long generalSlotDetailsId) throws WitcurveException;

    void deleteGeneralSlotDetails(Long generalSlotDetailsId) throws WitcurveException;

    List<GeneralSlotDetailsDTO> getGeneralSlotDetailsByStandardId(Long standardId) throws WitcurveException;
}
