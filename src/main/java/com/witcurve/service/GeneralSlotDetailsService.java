package com.witcurve.service;

import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface GeneralSlotDetailsService {

    List<GeneralSlotDetailsDTO> saveOrUpdate(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs);

    void clone(Long sourceClassId, List<Long> destinationClassIds);

    GeneralSlotDetailsDTO getGeneralSlotDetailsById(Long generalSlotDetailsId) throws WitcurveException;

    void deleteGeneralSlotDetails(Long generalSlotDetailsId) throws WitcurveException;

    List<GeneralSlotDetailsDTO> getGeneralSlotDetailsByClassId(Long classId) throws WitcurveException;
}
