package com.witcurve.service.impl;

import com.witcurve.domain.FeeDetails;
import com.witcurve.domain.SessionFeeStructure;
import com.witcurve.domain.enumeration.FeeDetailsType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.AcademicSessionRepository;
import com.witcurve.repository.FeeDetailsRepository;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.repository.SessionFeeStructureRepository;
import com.witcurve.service.SessionFeeStructureService;
import com.witcurve.service.dto.SessionFeeStructureDTO;
import com.witcurve.service.mapper.SessionFeeStructureMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SessionFeeStructureServiceImpl implements SessionFeeStructureService {

    private final Logger log = LoggerFactory.getLogger(SessionFeeStructureServiceImpl.class);

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    FeeDetailsRepository feeDetailsRepository;

    @Autowired
    SessionFeeStructureRepository sessionFeeStructureRepository;

    @Autowired
    SessionFeeStructureMapper sessionFeeStructureMapper;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Override
    public SessionFeeStructureDTO saveOrUpdate(SessionFeeStructureDTO sessionFeeStructureDTO) throws WitcurveException {
        log.debug("Request to save or update SessionFeeStructure : {}", sessionFeeStructureDTO);

        if (sessionFeeStructureDTO.getPenalty() != null) {
            if (sessionFeeStructureDTO.getDueDate() == null) {
                throw new WitcurveException("Due date is require if penalty is not null");
            }
        }
        Optional<FeeDetails> feeDetailsOfFeeType = feeDetailsRepository.findById(sessionFeeStructureDTO.getFeeTypeId());
        if (!feeDetailsOfFeeType.isPresent()) {
            throw new WitcurveException("No FeeDetails present with given feeTypeId : {}" + sessionFeeStructureDTO.getFeeTypeId());
        }
        if (!feeDetailsOfFeeType.get().getType().equals(FeeDetailsType.FEE_TYPE)) {
            throw new WitcurveException("Given FeeDetailsId is not of feeType");
        }
        Optional<FeeDetails> feeDetailsOfDescriptionType = feeDetailsRepository.findById(sessionFeeStructureDTO.getFeeDescriptionId());
        if (feeDetailsOfDescriptionType == null) {
            throw new WitcurveException("No FeeDetails present with given descriptionTypeId : {} " + sessionFeeStructureDTO.getFeeDescriptionId());
        }

        if (!feeDetailsOfDescriptionType.get().getType().equals(FeeDetailsType.FEE_DESCRIPTION)) {
            throw new WitcurveException("Given FeeDetailsId is not of feeDescriptionType");
        }
        SessionFeeStructure sessionFeeStructure = sessionFeeStructureRepository.save(sessionFeeStructureMapper.toEntity(sessionFeeStructureDTO));
        return sessionFeeStructureMapper.toDto(sessionFeeStructure);

    }

    @Override
    public SessionFeeStructureDTO getById(Long sessionFeeStructureId) {
        log.debug("Request to get SessionFeeStructure by id {} :" + sessionFeeStructureId);

        Optional<SessionFeeStructure> sessionFeeStructure = sessionFeeStructureRepository.findById(sessionFeeStructureId);
        if (!sessionFeeStructure.isPresent()) {
            throw new WitcurveException("No SessionFeeStructure is present with given id : {} " + sessionFeeStructureId);
        }
        return sessionFeeStructureMapper.toDto(sessionFeeStructure.get());
    }

    @Override
    public List<SessionFeeStructureDTO> getBySchoolInfoIdAndGrade(Long schoolInfoId, Grade grade) {
        log.debug("Request to get SessionFeeStructure with given schoolInfoId : {} and grade : {} ");

        List<SessionFeeStructure> sessionFeeStructures = sessionFeeStructureRepository.findByGradeAndSchoolInfoId(grade, schoolInfoId);
        return sessionFeeStructureMapper.toDto(sessionFeeStructures);
    }


    @Override
    public List<SessionFeeStructureDTO> getBySessionIdAndGrade(Long sessionId, Grade grade) {
        log.debug("Request to get SessionFeeStructure with given sessionId : {} and grade : {} ");

        List<SessionFeeStructure> sessionFeeStructures = sessionFeeStructureRepository.findByGradeAndSessionId(grade, sessionId);
        return sessionFeeStructureMapper.toDto(sessionFeeStructures);
    }

    @Override
    public void deleteById(Long sessionFeeStructureId) {
        log.debug(" Request to delete SessionFeeStructure with id : {}" + sessionFeeStructureId);

        Optional<SessionFeeStructure> sessionFeeStructure = sessionFeeStructureRepository.findById(sessionFeeStructureId);
        if (!sessionFeeStructure.isPresent()) {
            throw new WitcurveException("No SessionFeeStructure is present with given id : {}" + sessionFeeStructureId);
        }
        sessionFeeStructureRepository.deleteById(sessionFeeStructureId);
    }
}
