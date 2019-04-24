package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.StandardDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface StandardService {

    StandardDTO saveOrUpdateStandard(StandardDTO standardDTO) throws WitcurveException;

    StandardDTO getStandardById(Long id) throws WitcurveException;

    StandardDTO addClassTeacher(Long standardId, Long staffId) throws WitcurveException;

    List<StandardDTO> getStandardsBySchoolInfoId(Long schoolInfoId, Boolean slotAssigned);

    List<StandardDTO> getStandardsByTeacherId(Long teacherId);

    StandardDTO getStandard(Grade grade, String section, Long schoolInfoId) throws WitcurveException;

    void deleteStandard(Long standardId) throws WitcurveException;
}
