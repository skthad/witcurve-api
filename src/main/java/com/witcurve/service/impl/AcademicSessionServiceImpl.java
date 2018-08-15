package com.witcurve.service.impl;

import com.witcurve.domain.AcademicSession;
import com.witcurve.repository.AcademicSessionRepository;
import com.witcurve.service.AcademicSessionService;
import com.witcurve.service.dto.AcademicSessionDTO;
import com.witcurve.service.mapper.AcademicSessionMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcademicSessionServiceImpl implements AcademicSessionService {

    private final Logger log  = LoggerFactory.getLogger(AcademicSessionServiceImpl.class);

    @Autowired
    AcademicSessionMapper academicSessionMapper;

    @Autowired
    AcademicSessionRepository academicSessionRepository;


    @Override
    public AcademicSessionDTO saveOrUpdate(AcademicSessionDTO academicSessionDTO) {
        log.debug("Request to save or Update academic session {}", academicSessionDTO);
        AcademicSession academicSession = academicSessionMapper.toEntity(academicSessionDTO);
        academicSession = academicSessionRepository.save(academicSession);

        return academicSessionMapper.toDto(academicSession);
    }

    @Override
    public AcademicSessionDTO getAcademicSessionById(Long academicSessionId) throws WitcurveException {
        log.debug("Request to get academic session {}", academicSessionId);
        AcademicSession academicSession = academicSessionRepository.findById(academicSessionId).get();

        if (academicSession == null) {
            throw new WitcurveException("No Academic Session with given id");
        }

        return academicSessionMapper.toDto(academicSession);
    }
}
