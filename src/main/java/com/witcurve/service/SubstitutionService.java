package com.witcurve.service;

import com.witcurve.service.dto.SlotCourseDetailsDTO;
import com.witcurve.service.dto.SubstitutionDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface SubstitutionService {

    SubstitutionDTO substitute(SubstitutionDTO substitutionDTO) throws WitcurveException;

    HashMap<Long, List<SlotCourseDetailsDTO>> getSubstituteSuggestion(Long gsdId, Long teacherId, LocalDate date) throws WitcurveException;
}
