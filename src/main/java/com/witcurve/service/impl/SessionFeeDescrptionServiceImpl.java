package com.witcurve.service.impl;

import com.witcurve.domain.FeeDetails;
import com.witcurve.domain.SessionFeeDescription;
import com.witcurve.domain.enumeration.FeeDetailsType;
import com.witcurve.repository.FeeDetailsRepository;
import com.witcurve.repository.SessionFeeDescriptionRepository;
import com.witcurve.service.SessionFeeDescriptionService;
import com.witcurve.service.dto.SessionFeeDescriptionDTO;
import com.witcurve.service.mapper.SessionFeeDescriptionMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SessionFeeDescrptionServiceImpl implements SessionFeeDescriptionService {

    private final Logger log = LoggerFactory.getLogger(SessionFeeDescrptionServiceImpl.class);

    @Autowired
    FeeDetailsRepository feeDetailsRepository;

    @Autowired
    SessionFeeDescriptionMapper sessionFeeDescriptionMapper;

    @Autowired
    SessionFeeDescriptionRepository sessionFeeDescriptionRepository;

    @Override
    public SessionFeeDescriptionDTO saveOrUpdate(SessionFeeDescriptionDTO sessionFeeDescriptionDTO) {
        log.debug("Request to save or update sessionFeeStructureList : {} ", sessionFeeDescriptionDTO);
        Optional<FeeDetails> feeDetailOfFeeDescriptionType = feeDetailsRepository.findById(sessionFeeDescriptionDTO.getFeeDescriptionId());
        if (!feeDetailOfFeeDescriptionType.isPresent()) {
            throw new WitcurveException("No fee Detail is present with given id");
        }
        if (!feeDetailOfFeeDescriptionType.get().getType().equals(FeeDetailsType.FEE_DESCRIPTION)) {
            throw new WitcurveException("Given id is not of fee description type");
        }
        SessionFeeDescription sessionFeeDescription = sessionFeeDescriptionRepository.save(sessionFeeDescriptionMapper.toEntity(sessionFeeDescriptionDTO));
        return sessionFeeDescriptionMapper.toDto(sessionFeeDescription);
    }
}
