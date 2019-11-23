package com.witcurve.service.impl;

import com.witcurve.domain.FeeDetails;
import com.witcurve.domain.StudentFeeDescription;
import com.witcurve.domain.enumeration.FeeDetailsType;
import com.witcurve.repository.FeeDetailsRepository;
import com.witcurve.repository.StudentFeeDescriptionRepository;
import com.witcurve.service.StudentFeeDescriptionService;
import com.witcurve.service.dto.StudentFeeDescriptionDTO;
import com.witcurve.service.mapper.StudentFeeDescriptionMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentFeeDescriptionServiceImpl implements StudentFeeDescriptionService {

    private final Logger log = LoggerFactory.getLogger(StudentFeeDescriptionServiceImpl.class);

    @Autowired
    FeeDetailsRepository feeDetailsRepository;

    @Autowired
    StudentFeeDescriptionRepository studentFeeDescriptionRepository;

    @Autowired
    StudentFeeDescriptionMapper studentFeeDescriptionMapper;

    @Override
    public StudentFeeDescriptionDTO saveOrUpdate(StudentFeeDescriptionDTO studentFeeDescriptionDTO) {
        log.debug("Request to save or update StudentFeeDescription : {} ", studentFeeDescriptionDTO);
        Optional<FeeDetails> feeDetail = feeDetailsRepository.findById(studentFeeDescriptionDTO.getFeeDescriptionId());
        if(!feeDetail.isPresent()){
            throw new WitcurveException("No FeeDetail is present with given id : {} "+studentFeeDescriptionDTO.getId());
        }
        if(!feeDetail.get().getType().equals(FeeDetailsType.FEE_DESCRIPTION)){
            throw new WitcurveException("Given FeeDetail id is not of description type");
        }
        if(studentFeeDescriptionDTO.getAdjustment() + (studentFeeDescriptionDTO.getOneTimeDiscount())>studentFeeDescriptionDTO.getAmount()){
            throw new WitcurveException("Values are improper according to given amount") ;
        }
        StudentFeeDescription studentFeeDescription = studentFeeDescriptionRepository.save(studentFeeDescriptionMapper.toEntity(studentFeeDescriptionDTO));
        return studentFeeDescriptionMapper.toDto(studentFeeDescription);
    }
}
