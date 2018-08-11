package com.witcurve.service;

import com.witcurve.service.dto.ClassDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface ClassService {

    ClassDTO saveOrUpdateClass(ClassDTO classDTO);

    ClassDTO getClassById(Long id) throws WitcurveException;


}
