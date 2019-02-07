package com.witcurve.service;

import com.witcurve.service.dto.StaffDTO;
import com.witcurve.service.dto.SubstitutionDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface SubstitutionService {

    SubstitutionDTO substitute(SubstitutionDTO substitutionDTO) throws WitcurveException;

    List<StaffDTO> getSubstituteSuggestion(Long gsdId, Long teacherId, LocalDate date) throws WitcurveException;

    List<SubstitutionDTO> getSubstitutions(List<Long> gsdIds, LocalDate date);
}
