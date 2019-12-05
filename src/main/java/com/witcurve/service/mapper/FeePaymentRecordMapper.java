package com.witcurve.service.mapper;

import com.witcurve.domain.FeePaymentRecord;
import com.witcurve.service.dto.FeePaymentRecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentFeeStructureMapper.class,FeePaymentDetailMapper.class})
public interface FeePaymentRecordMapper extends EntityMapper<FeePaymentRecordDTO, FeePaymentRecord> {


    @Mapping(source = "studentFeeStructureId", target = "studentFeeStructure.id")
    FeePaymentRecord toEntity(FeePaymentRecordDTO feePaymentRecordDTO);

    @Mapping(target = "studentFeeStructureId", source = "studentFeeStructure.id")
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
