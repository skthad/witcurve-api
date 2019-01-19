package com.witcurve.service;

import com.witcurve.service.dto.TermDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface TermService {

    TermDTO createTerm(Long schoolInfoId, LocalDate termStartDate) throws WitcurveException;

    TermDTO updateTerm(TermDTO termDTO) throws WitcurveException;

    TermDTO getTermById(Long termId) throws WitcurveException;

    List<TermDTO> getTermsByAcademicSessionId(Long sessionId) throws WitcurveException;

    void deleteTerm(Long termId) throws WitcurveException;
}
