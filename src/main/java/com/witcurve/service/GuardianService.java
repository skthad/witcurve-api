package com.witcurve.service;

import com.witcurve.service.dto.GuardianDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface GuardianService {

    GuardianDTO saveOrUpdate(GuardianDTO guardianDTO) throws WitcurveException;

    GuardianDTO getGuardianById(Long guardianId) throws WitcurveException;

    List<GuardianDTO> getGuardiansByStudentId(Long studentId);

    void deleteGuardian(Long guardianId) throws WitcurveException;
}
