package com.witcurve.service;

import com.witcurve.service.dto.SurveySectionDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;
import java.util.Map;

public interface SurveySectionService {

    SurveySectionDTO saveOrUpdate(SurveySectionDTO surveySectionDTO) throws WitcurveException;

    void deleteOne(Long sectionId);

}
