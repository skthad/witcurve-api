package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.*;
import com.witcurve.repository.AttachmentRepository;
import com.witcurve.repository.FeeDetailsRepository;
import com.witcurve.repository.FeePaymentRecordRepository;
import com.witcurve.repository.StudentFeeStructureRepository;
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
    InvoiceUtil invoiceUtil;

    @Autowired
    FeeDetailsRepository feeDetailsRepository;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    AttachmentRepository attachmentRepository;

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
            //check it is require or not
            if (!mode.equals(ModeOfTransaction.SYSTEM)) {
                throw new WitcurveException("For payment through paytm mode should be system");
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

            if (type.equals(PaymentRecordType.PAYTM)) {
                if (feePaymentDetail.getItemId() == null) {
                    throw new WitcurveException("Item id is required for paytm transaction");
                }
            }
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
   //     InvoiceVM invoiceVM = prepareObject(feePaymentRecordDTO, studentFeeStructure.get().getStudent());
    //    File file = invoiceUtil.generateInvoice(invoiceVM);
  //      String destinationDirectory = AttachmentType.FEE_PAYMENT_RECORD.toString() + File.separator + feePaymentRecordDTO.getOrderId();
        //check with the file name if exist than delete  that attachment

     //   Attachment attachment = attachmentService.saveAttachmentWithFile(file, AttachmentType.FEE_PAYMENT_RECORD, destinationDirectory);
        TransactionRecordDTO transactionRecordDTO = new TransactionRecordDTO();
        transactionRecordDTO.setTransactionId(feePaymentRecordDTO.getTransactionId());
        transactionRecordDTO.setTransactionDate(feePaymentRecordDTO.getTransactionDate());
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

   /* private InvoiceVM prepareObject(FeePaymentRecordDTO feePaymentRecordDTO, Student student) {
        InvoiceVM invoiceVM = new InvoiceVM();
        invoiceVM.setStudent(student);
        invoiceVM.setInvoiceNo(feePaymentRecordDTO.getOrderId());
        Map<String, Double> feeDescriptionMap = new HashMap<>();

        //check this logic will create new Invoice with deleting older one and update fetch previous record and add in new one.
        //delete the previous attachment
        //here add Penalty as well

        FeePaymentRecord feePaymentRecords = feePaymentRecordRepository.getByOrderId(feePaymentRecordDTO.getTransactionId());
        List<FeePaymentDetailDTO> paidFeeDetails = new ArrayList<>();
        for (FeePaymentDetail feePaymentDetail : feePaymentRecords.getFeePaymentDetails()) {
            FeePaymentDetailDTO feePaymentDetailDTO = new FeePaymentDetailDTO();
            feePaymentDetailDTO.setFeeTypeId(feePaymentDetail.getFeeType().getId());
            feePaymentDetailDTO.setFeeDescriptionId(feePaymentDetail.getFeeDescription().getId());
            feePaymentDetailDTO.setAmount(feePaymentDetail.getAmount());
            paidFeeDetails.add(feePaymentDetailDTO);
        }

        feePaymentRecordDTO.getFeePaymentDetails().addAll(paidFeeDetails);
        List<Long> feeTypeIds = new ArrayList<>();

        for (FeePaymentDetailDTO feePaymentDetailDTO : feePaymentRecordDTO.getFeePaymentDetails()) {
            Optional<FeeDetails> feeDetail = feeDetailsRepository.findById(feePaymentDetailDTO.getFeeDescriptionId());

            if (feeTypeIds.size() > 0) {
                if (feeTypeIds.contains(feePaymentDetailDTO.getFeeTypeId())) {
                    if (feeDescriptionMap.containsKey(feeDetail.get().getName())) {
                        Double amount = feeDescriptionMap.get(feeDetail.get().getName());
                        Double newAmount = amount + feePaymentDetailDTO.getAmount();
                        feeDescriptionMap.put(feeDetail.get().getName(), newAmount);
                    }
                }
            }
            feeTypeIds.add(feePaymentDetailDTO.getFeeTypeId());
            feeDescriptionMap.put(feeDetail.get().getName(), feePaymentDetailDTO.getAmount());
        }
        invoiceVM.setFeeDescriptions(feeDescriptionMap);
        return invoiceVM;
    }*/
}

