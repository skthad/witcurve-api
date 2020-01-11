package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.*;
import com.witcurve.repository.*;
import com.witcurve.service.AttachmentService;
import com.witcurve.service.FeePaymentRecordService;
import com.witcurve.service.TransactionRecordService;
import com.witcurve.service.dto.FeePaymentDetailDTO;
import com.witcurve.service.dto.FeePaymentRecordDTO;
import com.witcurve.service.dto.TransactionRecordDTO;
import com.witcurve.service.mapper.FeePaymentRecordMapper;
import com.witcurve.service.util.InvoiceUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.InvoiceVM;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
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

    @Autowired
    TransactionRecordService transactionRecordService;

    @Autowired
    FeeDetailsRepository feeDetailsRepository;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    InvoiceUtil invoiceUtil;

    @Override
    public FeePaymentRecordDTO saveOrUpdate(FeePaymentRecordDTO feePaymentRecordDTO, ModeOfTransaction mode) {
        log.debug("Request to save or update FeePaymentRecord");
        formatAndValid(feePaymentRecordDTO, mode);
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

    private void formatAndValid(FeePaymentRecordDTO feePaymentRecordDTO, ModeOfTransaction mode) {
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
        PaymentRecordType type = feePaymentRecordDTO.getType();
        if (type.equals(PaymentRecordType.PAYTM)) {
            if (feePaymentRecordDTO.getTransactionId() == null) {
                throw new WitcurveException("Transaction id is required for paytm transaction");
            }
            if (!mode.equals(ModeOfTransaction.SYSTEM)) {
                throw new WitcurveException("For payment through paytm mode should be system");
            }
        }
        Optional<StudentFeeStructure> studentFeeStructure = studentFeeStructureRepository.findById(feePaymentRecordDTO.getStudentFeeStructureId());
        if (!studentFeeStructure.isPresent()) {
            throw new WitcurveException("No student fee structure is present with given id : {} " + feePaymentRecordDTO.getStudentFeeStructureId());
        }
        Map<Long, Map<Long, Double>> mapOfFeeTypeAndDescriptionIds = new HashMap<>();
        Map<Long, Double> mapOfPenalties = new HashMap<>();
        for (StudentFeeType studentFeeType : studentFeeStructure.get().getStudentFeeTypes()) {
            Long feeTypeId = studentFeeType.getFeeType().getId();
            Map<Long, Double> feeDescriptionIdAmountMap = new HashMap<>();
            for (StudentFeeDescription studentFeeDescription : studentFeeType.getStudentFeeDescriptions()) {
                Double amount;
                if (feePaymentRecordDTO.getFeePaymentType().equals(FeePaymentType.FULL_YEAR_PAYMENT)) {
                    amount = studentFeeDescription.getAmount() + studentFeeDescription.getAdjustment() - studentFeeDescription.getOneTimeDiscount();
                } else {
                    amount = studentFeeDescription.getAmount() + studentFeeDescription.getAdjustment();
                }
                feeDescriptionIdAmountMap.put(studentFeeDescription.getFeeDescription().getId(), amount);
            }
            mapOfFeeTypeAndDescriptionIds.put(feeTypeId, feeDescriptionIdAmountMap);
            mapOfPenalties.put(studentFeeType.getFeeType().getId(), studentFeeType.getPenalty());
        }
        List<Long> allFeeTypeIds = mapOfFeeTypeAndDescriptionIds.keySet().stream().collect(Collectors.toList());

        Double totalPaidAmount = 0.0;

        for (FeePaymentDetailDTO feePaymentDetail : feePaymentRecordDTO.getFeePaymentDetails()) {

            if (type.equals(PaymentRecordType.PAYTM)) {
                if (feePaymentRecordDTO.getFeePaymentType().equals(FeePaymentType.OUTSTANDING_FEE) && feePaymentDetail.getItemId() == null) {
                    throw new WitcurveException("Item id is required for paytm transaction of Outstanding Fee");
                }
            }
            if (!allFeeTypeIds.contains(feePaymentDetail.getFeeTypeId())) {
                throw new WitcurveException("Given fee type id is not in student fee structure");
            }
            if (feePaymentDetail.getFeeDescriptionId() != null) {
                Set<Long> allFeeDescriptionIds = mapOfFeeTypeAndDescriptionIds.get(feePaymentDetail.getFeeTypeId()).keySet();
                if (!allFeeDescriptionIds.contains(feePaymentDetail.getFeeDescriptionId())) {
                    throw new WitcurveException("Given fee description id is not present in student fee type");
                }
                Double amountInStudentFeeDescription = mapOfFeeTypeAndDescriptionIds.get(feePaymentDetail.getFeeTypeId()).get(feePaymentDetail.getFeeDescriptionId());
                if (!amountInStudentFeeDescription.equals(feePaymentDetail.getAmount())) {
                    throw new WitcurveException("Amount to be paid is not equal to amount given in student fee Description");
                }
            } else {
                if (!feePaymentDetail.getAmount().equals(mapOfPenalties.get(feePaymentDetail.getFeeTypeId()))) {
                    throw new WitcurveException("Amount to be paid for penalty is not equal to amount given in student fee type");
                }
            }
            totalPaidAmount = totalPaidAmount + feePaymentDetail.getAmount();
        }

        TransactionRecordDTO transactionRecordDTO = new TransactionRecordDTO();
        transactionRecordDTO.setTransactionId(feePaymentRecordDTO.getTransactionId());
        transactionRecordDTO.setTransactionDate(LocalDate.now());
        // transactionRecordDTO.setAttachments(Arrays.asList(attachment));
        transactionRecordDTO.setType(RecordType.FEE);
        transactionRecordDTO.setTransactionMode(mode);
        transactionRecordDTO.setTransactionType(TransactionType.CREDIT);
        transactionRecordDTO.setDescription("admissionId=" + studentFeeStructure.get().getStudent().getAdmissionId()
            + "/student=" + studentFeeStructure.get().getStudent().getFirstName() + "/totalPaidAmount=" + totalPaidAmount);
        transactionRecordDTO.setTotalAmount(totalPaidAmount);
        transactionRecordDTO.setSchoolInfoId(studentFeeStructure.get().getStudent().getSchoolInfo().getId());
        feePaymentRecordDTO.setTransactionRecordDTO(transactionRecordDTO);
    }

    @Override
    public File generateInvoice(Long feePaymentRecordId) {

        Optional<FeePaymentRecord> feePaymentRecord = feePaymentRecordRepository.findById(feePaymentRecordId);
        if (!feePaymentRecord.isPresent()) {
            throw new WitcurveException("No fee payment record is present with given id : {}" + feePaymentRecordId);
        }
        InvoiceVM invoiceVM = new InvoiceVM();
        invoiceVM.setStudent(feePaymentRecord.get().getStudentFeeStructure().getStudent());
        invoiceVM.setInvoiceNo(feePaymentRecord.get().getOrderId());

        Map<String, Double> feeDescriptionMap = new HashMap<>();

        for (FeePaymentDetail feePaymentDetail : feePaymentRecord.get().getFeePaymentDetails()) {
            String name = feePaymentDetail.getFeeDescription().getName();
            if (feeDescriptionMap.containsKey(name)) {
                Double amt = feeDescriptionMap.get(name) + feePaymentDetail.getAmount();
                feeDescriptionMap.put(name, amt);
            } else {
                feeDescriptionMap.put(name, feePaymentDetail.getAmount());
            }
        }
        invoiceVM.setFeeDescriptions(feeDescriptionMap);
        File invoice = invoiceUtil.generateInvoice(invoiceVM);
        return invoice;
    }
}

