package com.witcurve.service;

import com.witcurve.service.dto.AcademicSessionDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;

public interface AcademicSessionService {

    AcademicSessionDTO saveOrUpdate(AcademicSessionDTO academicSessionDTO) throws WitcurveException;

    AcademicSessionDTO getAcademicSessionById(Long academicSessionId) throws WitcurveException;

    AcademicSessionDTO getCurrentSessionByDate(Long schoolInfoId, LocalDate date);

    void deleteAcademicSession(Long academicSessionId) throws WitcurveException;
}
