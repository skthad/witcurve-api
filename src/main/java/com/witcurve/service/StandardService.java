package com.witcurve.service;

import com.witcurve.service.dto.StandardDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface StandardService {

    StandardDTO saveOrUpdateStandard(StandardDTO standardDTO);

    StandardDTO getStandardById(Long id) throws WitcurveException;

    List<StandardDTO> getStandardsByTeacherIdAndTermId(Long teacherId, Long termId) throws WitcurveException;

    void deleteStandard(Long standardId) throws WitcurveException;


}
