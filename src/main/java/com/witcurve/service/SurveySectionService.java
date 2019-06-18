package com.witcurve.service;

import com.witcurve.service.dto.SurveySectionDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface SurveySectionService {

    SurveySectionDTO saveOrUpdate(SurveySectionDTO surveySectionDTO) throws WitcurveException;

    void deleteOne(Long surveyFormId);
}
