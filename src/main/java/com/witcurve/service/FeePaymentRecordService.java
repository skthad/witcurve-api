package com.witcurve.service;

import com.witcurve.domain.enumeration.ModeOfTransaction;
import com.witcurve.service.dto.FeePaymentRecordDTO;

import java.io.File;
import java.util.List;

public interface FeePaymentRecordService {

    FeePaymentRecordDTO saveOrUpdate(FeePaymentRecordDTO feePaymentRecordDTO, ModeOfTransaction mode);

    List<FeePaymentRecordDTO> getByStudentAndSessionId(Long studentId, Long sessionId);

    List<FeePaymentRecordDTO> getByStandardAndSessionId(Long standardId, Long sessionId);

    File generateInvoice(Long feePaymentRecordId);

    void deleteOne(Long id);
}
