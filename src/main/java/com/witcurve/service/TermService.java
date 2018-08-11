package com.witcurve.service;

import com.witcurve.service.dto.TermDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface TermService {

    TermDTO saveOrUpdate(TermDTO termDTO);

    TermDTO getTermById(Long termId) throws WitcurveException;

    void deleteTerm(Long termId) throws WitcurveException;
}
