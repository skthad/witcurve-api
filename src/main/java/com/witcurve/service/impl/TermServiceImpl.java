package com.witcurve.service.impl;

import com.witcurve.domain.AcademicSession;
import com.witcurve.domain.Term;
import com.witcurve.repository.AcademicSessionRepository;
import com.witcurve.repository.TermRepository;
import com.witcurve.service.TermService;
import com.witcurve.service.dto.TermDTO;
import com.witcurve.service.mapper.TermMapper;
import com.witcurve.service.mapper.TermMapperLite;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TermServiceImpl implements TermService {

    private final Logger log  = LoggerFactory.getLogger(TermServiceImpl.class);

    @Autowired
    TermRepository termRepository;

    @Autowired
    TermMapper termMapper;

    @Autowired
    TermMapperLite termMapperLite;

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Override
    public TermDTO createTerm(Long schoolInfoId, LocalDate termStartDate) throws WitcurveException {
        AcademicSession nearestSessionToTerm = academicSessionRepository
            .nearestSessionToTerm(schoolInfoId, termStartDate);
        if (nearestSessionToTerm == null) {
            throw new WitcurveException("No academic session found to link the term start date");
        }

        List<Term> existingTerms = termRepository.findTermsInSession(nearestSessionToTerm.getId());

        // if no existing terms in the session then start date should match, i.e, first term being created
        // in an academic session should have same start date as term start date
        if (existingTerms.size() == 0 && !termStartDate.equals(nearestSessionToTerm.getStartDate())) {
            throw new WitcurveException("First term should have the same start date as academic session start date.");
        } else if (existingTerms.size() > 0 &&
            !existingTerms.get(0).getStartDate().equals(nearestSessionToTerm.getStartDate()) &&
            !termStartDate.equals(nearestSessionToTerm.getStartDate())) {
            throw new WitcurveException("Please first create a term with start date as session start date");
        }

        Term term = new Term();
        term.setStartDate(termStartDate);
        term.setSession(nearestSessionToTerm);
        term =  termRepository.save(term);
        return termMapper.toDto(term);
    }

    @Override
    public TermDTO updateTerm(TermDTO termDTO) throws WitcurveException {
        AcademicSession nearestSessionToTerm = academicSessionRepository
            .nearestSessionToTerm(termDTO.getSession().getSchoolInfo().getId(), termDTO.getStartDate());
        if (nearestSessionToTerm == null || !nearestSessionToTerm.getId().equals(termDTO.getSession().getId())) {
            throw new WitcurveException("Term start date out of boundary values with provided session.");
        }
        List<Term> existingTerms = termRepository.findTermsInSession(termDTO.getSession().getId());

        // if no existing terms in the session then start date should match, i.e, first term being created
        // in an academic session should have same start date as term start date
        if (existingTerms.size() == 0 && !termDTO.getStartDate().equals(termDTO.getSession().getStartDate())) {
            throw new WitcurveException("First term should have the same start date as academic session start date.");
        } else if (existingTerms.size() > 0 &&
            termDTO.getId().equals(existingTerms.get(0).getId()) &&
            !existingTerms.get(0).getStartDate().equals(termDTO.getStartDate())) {
            throw new WitcurveException("Cannot update the start date of the first term.");
        } else if (existingTerms.size() > 0 &&
            !termDTO.getId().equals(existingTerms.get(0).getId()) &&
            existingTerms.get(0).getStartDate().equals(termDTO.getStartDate())) {
            throw new WitcurveException("A term already exists with this date");
        }

        Term term = termMapper.toEntity(termDTO);
        term = termRepository.save(term);
        return termMapper.toDto(term);
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
    public List<TermDTO> getTermsByAcademicSessionId(Long sessionId) throws WitcurveException {
        log.debug("Request to get terms with academic session id : {}", sessionId);
        List<Term> terms = termRepository.findTermsInSession(sessionId);

        return termMapperLite.toDto(terms);
    }

    @Override
    public void deleteTerm(Long termId) throws WitcurveException {
        log.debug("Request to delete term with id : {}", termId);
        Term term = termRepository.findById(termId).get();

        if (term == null){
            throw new WitcurveException("No term with given Id");
        } else if (term.getStartDate().equals(term.getSession().getStartDate())) {
            throw new WitcurveException("Cannot delete the first term of the academic session.");
        }
        termRepository.delete(term);
    }
}
