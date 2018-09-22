package com.witcurve.service;

import com.witcurve.service.dto.TermDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface TermService {

    List<TermDTO> saveOrUpdate(List<TermDTO> termDTOs);

    TermDTO getTermById(Long termId) throws WitcurveException;

    void deleteTerm(Long termId) throws WitcurveException;
}
