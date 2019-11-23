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
import com.witcurve.service.dto.SessionFeeDescriptionDTO;
import com.witcurve.service.dto.SessionFeeStructureDTO;
import com.witcurve.service.mapper.SessionFeeStructureMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<SessionFeeStructureDTO> saveOrUpdate(List<SessionFeeStructureDTO> sessionFeeStructureDTOs, Grade grade, Long sessionId) throws WitcurveException {
        log.debug("Request to save or update SessionFeeStructures : {} for grade : {} and for session with id : {}", sessionFeeStructureDTOs, grade, sessionId);
        for (SessionFeeStructureDTO sessionFeeStructureDTO : sessionFeeStructureDTOs) {

            Optional<FeeDetails> feeDetailsOfFeeType = feeDetailsRepository.findById(sessionFeeStructureDTO.getFeeTypeId());
            if (!feeDetailsOfFeeType.isPresent()) {
                throw new WitcurveException("No fee details present with given feeTypeId : {}" + sessionFeeStructureDTO.getFeeTypeId());
            }
            if (!feeDetailsOfFeeType.get().getType().equals(FeeDetailsType.FEE_TYPE)) {
                throw new WitcurveException("Given id is not of fee type");
            }
            List<SessionFeeDescriptionDTO> sessionFeeDescriptions = sessionFeeStructureDTO.getSessionFeeDescriptions();
            if (sessionFeeDescriptions.size() == 0) {
                throw new WitcurveException("Minimum one record of session fee description is require");
            }
            List<Long> feeDescriptionIds = new ArrayList<>();
            for (SessionFeeDescriptionDTO sessionFeeDescription : sessionFeeDescriptions) {
                feeDescriptionIds.add(sessionFeeDescription.getFeeDescriptionId());
            }
            List<FeeDetails> feeDescriptions = feeDetailsRepository.findBySchoolInfoIdAndType(feeDetailsOfFeeType.get().getSchoolInfo().getId(), FeeDetailsType.FEE_DESCRIPTION);

            List<Long> allFeeDescriptionIds = new ArrayList<>();
            for (FeeDetails feeDescription : feeDescriptions) {
                allFeeDescriptionIds.add(feeDescription.getId());
            }
            if (!allFeeDescriptionIds.containsAll(feeDescriptionIds)) {
                throw new WitcurveException("Given id is not of fee description type");
            }
            sessionFeeStructureDTO.setGrade(grade);
            sessionFeeStructureDTO.setSessionId(sessionId);
        }
        List<SessionFeeStructure> sessionFeeStructures = sessionFeeStructureRepository.saveAll(sessionFeeStructureMapper.toEntity(sessionFeeStructureDTOs));
        return sessionFeeStructureMapper.toDto(sessionFeeStructures);
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
