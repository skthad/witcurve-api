package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.SessionFeeStructureDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface SessionFeeStructureService {

    SessionFeeStructureDTO saveOrUpdate(SessionFeeStructureDTO sessionFeeStructureDTO) throws WitcurveException;

    SessionFeeStructureDTO getById(Long sessionFeeStructureId);

    List<SessionFeeStructureDTO> getBySchoolInfoIdAndGrade(Long schoolInfoId, Grade grade);

    void deleteById(Long sessionFeeStructureId);


}
