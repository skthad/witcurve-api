package com.witcurve.service.impl;

import com.witcurve.domain.FeePaymentRecord;
import com.witcurve.domain.StudentFeeDescription;
import com.witcurve.domain.StudentFeeStructure;
import com.witcurve.domain.StudentFeeType;
import com.witcurve.domain.enumeration.PaymentRecordType;
import com.witcurve.repository.FeePaymentRecordRepository;
import com.witcurve.repository.StudentFeeStructureRepository;
import com.witcurve.service.FeePaymentRecordService;
import com.witcurve.service.dto.FeePaymentDetailDTO;
import com.witcurve.service.dto.FeePaymentRecordDTO;
import com.witcurve.service.mapper.FeePaymentRecordMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeePaymentRecordServiceImpl implements FeePaymentRecordService {

    private final Logger log = LoggerFactory.getLogger(FeePaymentRecordServiceImpl.class);

    @Autowired
    FeePaymentRecordRepository feePaymentRecordRepository;

    @Autowired
    FeePaymentRecordMapper feePaymentRecordMapper;

    @Autowired
    StudentFeeStructureRepository studentFeeStructureRepository;

    @Override
    public FeePaymentRecordDTO saveOrUpdate(FeePaymentRecordDTO feePaymentRecordDTO) {
        log.debug("Request to save or update FeePaymentRecord");
        formatAndValid(feePaymentRecordDTO);
        FeePaymentRecord feePaymentRecord = feePaymentRecordRepository.save(feePaymentRecordMapper.toEntity(feePaymentRecordDTO));
        return feePaymentRecordMapper.toDto(feePaymentRecord);
    }

    @Override
    public List<FeePaymentRecordDTO> getByStudentAndSessionId(Long studentId, Long sessionId) {
        log.debug("Request to get FeePaymentRecord by studentId and sessionId : {} ", studentId, sessionId);
        List<FeePaymentRecord> feePaymentRecords = feePaymentRecordRepository.getByStudentAndSessionId(studentId, sessionId);
        return feePaymentRecordMapper.toDto(feePaymentRecords);
    }

    @Override
    public List<FeePaymentRecordDTO> getByStandardAndSessionId(Long standardId, Long sessionId) {
        log.debug("Request to get FeePaymentRecord by standardId and sessionId : {} ", standardId, sessionId);
        List<FeePaymentRecord> feePaymentRecords = feePaymentRecordRepository.getByStandardAndSessionId(standardId, sessionId);
        return feePaymentRecordMapper.toDto(feePaymentRecords);
    }

    @Override
    public void deleteOne(Long id) {
        log.debug("Request to delete record with id : {} ", id);
        Optional<FeePaymentRecord> feePaymentRecord = feePaymentRecordRepository.findById(id);
        if (!feePaymentRecord.isPresent()) {
            throw new WitcurveException("No record is present with given id");
        }
        if (feePaymentRecord.get().getType().equals(PaymentRecordType.PAYTM)) {
            throw new WitcurveException("Can not delete record when payment is done through paytm");
        }
        feePaymentRecordRepository.delete(feePaymentRecord.get());
    }

    private void formatAndValid(FeePaymentRecordDTO feePaymentRecordDTO) {
        if (feePaymentRecordDTO.getId() != null) {
            Optional<FeePaymentRecord> feePaymentRecord = feePaymentRecordRepository.findById(feePaymentRecordDTO.getId());
            if (!feePaymentRecord.isPresent()) {
                throw new WitcurveException("No fee payment record is present with given id :{} " + feePaymentRecordDTO.getId());
            }
            if (feePaymentRecordDTO.getOrderId() == null || !feePaymentRecord.get().getOrderId().equals(feePaymentRecordDTO.getOrderId())) {
                throw new WitcurveException("While updating record order id is require and if given should not be changed");
            }
        } else {
            feePaymentRecordDTO.setOrderId(RandomStringUtils.randomAlphanumeric(8));
        }
        if (feePaymentRecordDTO.getType().equals(PaymentRecordType.PAYTM)) {
            if (feePaymentRecordDTO.getPaytmId() == null) {
                throw new WitcurveException("Paytm id is require for paytm transaction");
            }
        }
        Optional<StudentFeeStructure> studentFeeStructure = studentFeeStructureRepository.findById(feePaymentRecordDTO.getStudentFeeStructureId());
        if (!studentFeeStructure.isPresent()) {
            throw new WitcurveException("No student fee structure is present with given id : {} " + feePaymentRecordDTO.getStudentFeeStructureId());
        }
        Map<Long, List<Long>> mapOfFeeTypeAndDescriptionIds = new HashMap<>();
        for (StudentFeeType studentFeeType : studentFeeStructure.get().getStudentFeeTypes()) {
            Long feeTypeId = studentFeeType.getFeeType().getId();
            List<Long> feeDescriptionIds = new ArrayList<>();
            for (StudentFeeDescription studentFeeDescription : studentFeeType.getStudentFeeDescriptions()) {
                feeDescriptionIds.add(studentFeeDescription.getFeeDescription().getId());
            }
            mapOfFeeTypeAndDescriptionIds.put(feeTypeId, feeDescriptionIds);
        }
        List<Long> allFeeTypeIds = mapOfFeeTypeAndDescriptionIds.keySet().stream().collect(Collectors.toList());
        Double totalPaidAmount = 0.0;
        for (FeePaymentDetailDTO feePaymentDetail : feePaymentRecordDTO.getFeePaymentDetails()) {

            if (feePaymentDetail.getAmount() < 0) {
                throw new WitcurveException("Amount must be positive");
            }
            if (!allFeeTypeIds.contains(feePaymentDetail.getFeeTypeId())) {
                throw new WitcurveException("Given fee type id is not in student fee structure");
            }
            List<Long> allFeeDescriptionIds = mapOfFeeTypeAndDescriptionIds.get(feePaymentDetail.getFeeTypeId());
            if (!allFeeDescriptionIds.contains(feePaymentDetail.getFeeDescriptionId())) {
                throw new WitcurveException("Given fee description id is not present in student fee type");
            }
            totalPaidAmount = totalPaidAmount + feePaymentDetail.getAmount();
        }
        if (feePaymentRecordDTO.getTotalAmount() != totalPaidAmount + feePaymentRecordDTO.getPenaltyAmount()) {
            throw new WitcurveException("Total amount is not according to penalty amount and each fee description amount");
        }
    }

}

