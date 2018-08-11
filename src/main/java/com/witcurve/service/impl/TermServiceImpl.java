package com.witcurve.service.impl;

import com.witcurve.domain.Term;
import com.witcurve.repository.TermRepository;
import com.witcurve.service.TermService;
import com.witcurve.service.dto.TermDTO;
import com.witcurve.service.mapper.TermMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TermServiceImpl implements TermService {

    private final Logger log  = LoggerFactory.getLogger(TermServiceImpl.class);

    @Autowired
    TermRepository termRepository;

    @Autowired
    TermMapper termMapper;


    @Override
    public TermDTO saveOrUpdate(TermDTO termDTO) {
        log.debug("Request to save or update term : {}", termDTO);
        Term term = termMapper.termDTOToTerm(termDTO);
        term =  termRepository.save(term);
        return termMapper.termToTermDTO(term);
}

    @Override
    public TermDTO getTermById(Long termId) throws WitcurveException {
        log.debug("Request to get term with id : {}", termId);
        Term term = termRepository.findById(termId).get();

        if (term ==  null) {
           throw new WitcurveException("No term with given Id");
        }
        return termMapper.termToTermDTO(term);
    }

    @Override
    public void deleteTerm(Long termId) throws WitcurveException {
        log.debug("Request to delete term with id : {}", termId);
        Term term = termRepository.findById(termId).get();

        if (term == null){
            throw new WitcurveException("No term with given Id");
        }
        termRepository.delete(term);
    }
}
