package com.witcurve.service.mapper;

import com.witcurve.domain.FeePaymentRecord;
import com.witcurve.service.dto.FeePaymentRecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentFeeStructureMapper.class,FeePaymentDetailMapper.class,TransactionRecordMapper.class})
public interface FeePaymentRecordMapper extends EntityMapper<FeePaymentRecordDTO, FeePaymentRecord> {


    @Mapping(source = "studentFeeStructureId", target = "studentFeeStructure.id")
    @Mapping(source = "transactionRecordDTO", target = "transactionRecord")
    FeePaymentRecord toEntity(FeePaymentRecordDTO feePaymentRecordDTO);

    @Mapping(target = "studentFeeStructureId", source = "studentFeeStructure.id")
    @Mapping(target = "transactionRecordDTO", source = "transactionRecord")
    FeePaymentRecordDTO toDto(FeePaymentRecord feePaymentRecord);

    default FeePaymentRecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        FeePaymentRecord feePaymentRecord = new FeePaymentRecord();
        feePaymentRecord.setId(id);
        return feePaymentRecord;
    }
}
