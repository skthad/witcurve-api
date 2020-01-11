package com.witcurve.service.mapper;

import com.witcurve.domain.FeePaymentDetail;
import com.witcurve.domain.FeePaymentRecord;
import com.witcurve.service.dto.FeePaymentRecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring", uses = {StudentFeeStructureMapper.class, FeePaymentDetailMapper.class, TransactionRecordMapper.class})
public interface FeePaymentRecordMapper extends EntityMapper<FeePaymentRecordDTO, FeePaymentRecord> {


    @Mapping(source = "studentFeeStructureId", target = "studentFeeStructure")
    @Mapping(source = "transactionRecordDTO", target = "transactionRecord")
    FeePaymentRecord toEntity(FeePaymentRecordDTO feePaymentRecordDTO);

    @Mapping(target = "studentFeeStructureId", source = "studentFeeStructure.id")
    @Mapping(target = "transactionRecordDTO", source = "transactionRecord")
    @Mapping(target = "transactionDate", expression = "java(getFirstTransactionDate(feePaymentRecord))")
    @Mapping(target = "totalAmount", expression = "java(getTotalAmount(feePaymentRecord))")
    FeePaymentRecordDTO toDto(FeePaymentRecord feePaymentRecord);

    default FeePaymentRecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        FeePaymentRecord feePaymentRecord = new FeePaymentRecord();
        feePaymentRecord.setId(id);
        return feePaymentRecord;
    }

    default LocalDate getFirstTransactionDate(FeePaymentRecord feePaymentRecord) {
        LocalDate minDate = null;
        for (FeePaymentDetail feePaymentDetail : feePaymentRecord.getFeePaymentDetails()) {
            if (minDate == null) {
                minDate = feePaymentDetail.getTransactionDate();
            } else {
                if (feePaymentDetail.getTransactionDate().compareTo(minDate) < 0) {
                    minDate = feePaymentDetail.getTransactionDate();
                }
            }
        }
        return minDate;
    }

    default Double getTotalAmount(FeePaymentRecord feePaymentRecord) {
        Double totalAmount = 0.0;
        for (FeePaymentDetail feePaymentDetail : feePaymentRecord.getFeePaymentDetails()) {
            totalAmount = totalAmount + feePaymentDetail.getAmount();
        }
        return totalAmount;
    }
}
