package com.witcurve.service.impl;

import com.witcurve.domain.AcademicSession;
import com.witcurve.domain.Term;
import com.witcurve.repository.AcademicSessionRepository;
import com.witcurve.repository.TermRepository;
import com.witcurve.service.AcademicSessionService;
import com.witcurve.service.dto.AcademicSessionDTO;
import com.witcurve.service.mapper.AcademicSessionMapper;
import com.witcurve.service.mapper.AcademicSessionMapperLite;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AcademicSessionServiceImpl implements AcademicSessionService {

    private final Logger log  = LoggerFactory.getLogger(AcademicSessionServiceImpl.class);

    @Autowired
    AcademicSessionMapper academicSessionMapper;

    @Autowired
    AcademicSessionMapperLite academicSessionMapperLite;

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    TermRepository termRepository;

    @Override
    public AcademicSessionDTO saveOrUpdate(AcademicSessionDTO academicSessionDTO) throws WitcurveException {
        log.debug("Request to save or Update academic session {}", academicSessionDTO);
        LocalDate newStartDateOfSession = academicSessionDTO.getStartDate();

        AcademicSession previousSession = academicSessionRepository.getPreviousSession(academicSessionDTO.getSchoolInfo().getId(), academicSessionDTO.getStartDate());
        if (previousSession != null) {
            List<Term> termsInPreviousSession = termRepository.findTermsInSession(previousSession.getId());
            if (termsInPreviousSession.size() > 0) {
                if (!newStartDateOfSession.isAfter(termsInPreviousSession.get(termsInPreviousSession.size() - 1).getStartDate())) {
                    throw new WitcurveException("A session cannot start before the last term in the previous session starts.");
                }
            }
        }

        if (academicSessionDTO.getId() != null) {
            List<Term> termsInExistingSession = termRepository.findTermsInSession(academicSessionDTO.getId());

            //start date must be less than first term start date in termsInSession
            if (termsInExistingSession.size() == 1) {
                termsInExistingSession.get(0).setStartDate(newStartDateOfSession);
            } else if (termsInExistingSession.size() > 1) {
                if (!newStartDateOfSession.isBefore(termsInExistingSession.get(1).getStartDate())) {
                    throw new WitcurveException("There is already a term with start date on before new start date for session.");
                }
            }
        }
        AcademicSession academicSession = academicSessionMapper.toEntity(academicSessionDTO);
        academicSession = academicSessionRepository.save(academicSession);

        if (academicSessionDTO.getId() == null) {
            Term term = new Term();
            term.setStartDate(academicSession.getStartDate());
            term.setSession(academicSession);
            termRepository.save(term);

            log.debug("Automatically created first term for the new acadmic session");
        }
        return academicSessionMapper.toDto(academicSession);
    }

    @Override
    public AcademicSessionDTO getAcademicSessionById(Long academicSessionId) throws WitcurveException {
        log.debug("Request to get academic session {}", academicSessionId);
        Optional<AcademicSession> academicSession = academicSessionRepository.findById(academicSessionId);
        if (!academicSession.isPresent()) {
            throw new WitcurveException("No Academic Session with given id");
        }

        return academicSessionMapper.toDto(academicSession.get());
    }

    @Override
    public List<AcademicSessionDTO> getAcademicSessionsBySchoolInfoId(Long schoolInfoId) {
        log.debug("Request to get all academic sessions in schoolInfoId " + schoolInfoId);
        List<AcademicSession> sessions = academicSessionRepository.getAllSessionsInSchoolInfo(schoolInfoId);
        return academicSessionMapperLite.toDto(sessions);
    }

    @Override
    public void deleteAcademicSession(Long academicSessionId) throws WitcurveException {
        log.debug("Request to delete academicSession with id {}", academicSessionId);
        Optional<AcademicSession> academicSession = academicSessionRepository.findById(academicSessionId);
        if (!academicSession.isPresent()) {
            throw new WitcurveException("No Academic Session with given id");
        }
        academicSessionRepository.delete(academicSession.get());
    }

    @Override
    public AcademicSessionDTO getCurrentSessionByDate(Long schoolInfoId, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        AcademicSession currentSession = academicSessionRepository.nearestSessionToDate(schoolInfoId, date);
        return academicSessionMapper.toDto(currentSession);
    }

    @Override
    public AcademicSessionDTO getNextSessionSessionAfterDate(Long schoolInfoId, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        AcademicSession currentSession = academicSessionRepository.nextSessionAfterDate(schoolInfoId, date);
        return academicSessionMapper.toDto(currentSession);
    }
}
