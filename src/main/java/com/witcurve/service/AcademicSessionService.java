package com.witcurve.service;

import com.witcurve.service.dto.AcademicSessionDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface AcademicSessionService {

    AcademicSessionDTO saveOrUpdate(AcademicSessionDTO academicSessionDTO) throws WitcurveException;

    AcademicSessionDTO getAcademicSessionById(Long academicSessionId) throws WitcurveException;

    List<AcademicSessionDTO> getAcademicSessionsBySchoolInfoId(Long schoolInfoId);

    AcademicSessionDTO getCurrentSessionByDate(Long schoolInfoId, LocalDate date);

    AcademicSessionDTO getNextActiveSessionAfterDate(Long schoolInfoId, LocalDate date);

    void deleteAcademicSession(Long academicSessionId) throws WitcurveException;
}
