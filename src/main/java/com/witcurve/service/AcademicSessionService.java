package com.witcurve.service;

import com.witcurve.service.dto.AcademicSessionDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface AcademicSessionService {

    AcademicSessionDTO saveOrUpdate(AcademicSessionDTO academicSessionDTO);

    AcademicSessionDTO getAcademicSessionById(Long academicSessionId) throws WitcurveException;
}
