package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.SessionFeeStructureDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface SessionFeeStructureService {

    List<SessionFeeStructureDTO> saveOrUpdate(List<SessionFeeStructureDTO> sessionFeeStructureDTOs, Grade grade, Long sessionId) throws WitcurveException;

    SessionFeeStructureDTO getById(Long sessionFeeStructureId);

    List<SessionFeeStructureDTO> getBySchoolInfoIdAndGrade(Long schoolInfoId, Grade grade);

    void deleteById(Long sessionFeeStructureId);

    List<SessionFeeStructureDTO> getBySessionIdAndGrade(Long sessionId, Grade grade);


}
