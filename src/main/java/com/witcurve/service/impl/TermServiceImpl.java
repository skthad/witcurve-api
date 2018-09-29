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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TermServiceImpl implements TermService {

    private final Logger log  = LoggerFactory.getLogger(TermServiceImpl.class);

    @Autowired
    TermRepository termRepository;

    @Autowired
    TermMapper termMapper;


    @Override
    public List<TermDTO> saveOrUpdate(List<TermDTO> termDTOs) {
        log.debug("Request to save or update terms : {}", termDTOs);
        List<Term> terms = termMapper.toEntity(termDTOs);
        terms =  termRepository.saveAll(terms);
        return termMapper.toDto(terms);
}

    @Override
    public TermDTO getTermById(Long termId) throws WitcurveException {
        log.debug("Request to get term with id : {}", termId);
        Term term = termRepository.findById(termId).get();

        if (term ==  null) {
           throw new WitcurveException("No term with given Id");
        }
        return termMapper.toDto(term);
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
