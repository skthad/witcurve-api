package com.witcurve.service.impl;

import com.witcurve.domain.Attachment;
import com.witcurve.domain.TransactionRecord;
import com.witcurve.domain.enumeration.AttachmentType;
import com.witcurve.domain.enumeration.ModeOfTransaction;
import com.witcurve.domain.enumeration.RecordType;
import com.witcurve.domain.enumeration.TransactionType;
import com.witcurve.repository.TransactionRecordRepository;
import com.witcurve.service.AttachmentService;
import com.witcurve.service.TransactionRecordService;
import com.witcurve.service.dto.TransactionRecordDTO;
import com.witcurve.service.mapper.TransactionRecordMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionRecordServiceImpl implements TransactionRecordService {

    private final Logger log = LoggerFactory.getLogger(TransactionRecordServiceImpl.class);

    @Autowired
    TransactionRecordRepository transactionRecordRepository;

    @Autowired
    TransactionRecordMapper transactionRecordMapper;

    @Autowired
    AttachmentService attachmentService;

    @Override
    public TransactionRecordDTO saveOrUpdate(TransactionRecordDTO transactionRecordDTO) {
        log.debug("Request to save or update TransactionRecord : {} ", transactionRecordDTO);
        isValid(transactionRecordDTO);
        TransactionRecord transactionRecord = transactionRecordRepository.save(transactionRecordMapper.toEntity(transactionRecordDTO));
        return transactionRecordMapper.toDto(transactionRecord);
    }

    @Override
    public TransactionRecordDTO getById(Long id) {
        log.debug("Request to get TransactionRecord of id: {} ", id);
        Optional<TransactionRecord> transactionRecord = transactionRecordRepository.findById(id);
        if (!transactionRecord.isPresent()) {
            throw new WitcurveException("No transaction record is present with given id");
        }
        return transactionRecordMapper.toDto(transactionRecord.get());
    }

    @Override
    public Page<TransactionRecordDTO> getBySchoolInfoIdAndDateRange(Pageable pageable, Long schoolInfoId, LocalDate fromDate, LocalDate endDate) {
        log.debug("Request to get TransactionRecord with given schoolInfoId and date range: {} ", schoolInfoId, fromDate, endDate);
        Page<TransactionRecord> transactionRecords = transactionRecordRepository.getBySchoolInfoAndTransactionDate(schoolInfoId, fromDate, endDate, pageable);
        return transactionRecords.map(transactionRecordMapper::toDto);
    }

    @Override
    public TransactionRecordDTO addAttachment(Long id, List<MultipartFile> files) {
        log.debug("Request to add attachment to TransactionRecord with given id {} ", id);
        Optional<TransactionRecord> transactionRecord = transactionRecordRepository.findById(id);
        if (!transactionRecord.isPresent()) {
            throw new WitcurveException("No transaction record is present with given id : {} " + id);
        }
        List<Attachment> listOfAttachment = new ArrayList<>();
        AttachmentType type = null;
        Attachment attachment = null;
        String directoryName = " ";

        if (transactionRecord.get().getType().equals(RecordType.FEE)) {
            type = AttachmentType.FEE_PAYMENT_RECORD;
            directoryName = type.toString() + File.separator + transactionRecord.get().getId();
        } else if (transactionRecord.get().getType().equals(RecordType.PAYROLL)) {
            type = AttachmentType.STAFF_PAYROLL;
            directoryName = type.toString() + File.separator + transactionRecord.get().getId();
        }

        for (MultipartFile file : files) {
            attachment = attachmentService.saveAttachmentWithMultipart(file, type, directoryName);
            listOfAttachment.add(attachment);
        }
        transactionRecord.get().addAttachment(listOfAttachment);
        return transactionRecordMapper.toDto(transactionRecord.get());
    }

    private void isValid(TransactionRecordDTO transactionRecordDTO) {
        if (transactionRecordDTO.getType().equals(RecordType.FEE)) {
            if (!transactionRecordDTO.getTransactionType().equals(TransactionType.CREDIT)) {
                throw new WitcurveException("Fee can not be debited");
            }
        }
        if (!transactionRecordDTO.getTransactionMode().equals(ModeOfTransaction.CASH)) {
            if (transactionRecordDTO.getTransactionId() == null) {
                throw new WitcurveException("Transaction id can not be null when transaction is online or through cheque or through dd");
            }
        }

    }
}
