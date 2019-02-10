package com.witcurve.service;

import com.witcurve.service.dto.StandardDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface StandardService {

    StandardDTO saveOrUpdateStandard(StandardDTO standardDTO) throws WitcurveException;

    StandardDTO getStandardById(Long id) throws WitcurveException;

    List<StandardDTO> getStandardsBySchoolInfoId(Long schoolInfoId, Boolean slotAssigned);

    List<StandardDTO> getStandardsByTeacherId(Long teacherId);

    void deleteStandard(Long standardId) throws WitcurveException;
}
