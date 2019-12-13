package com.witcurve.service.mapper;

import com.witcurve.domain.TransactionRecord;
import com.witcurve.service.dto.TransactionRecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface TransactionRecordMapper extends EntityMapper<TransactionRecordDTO, TransactionRecord> {

    @Mapping(source = "schoolInfo.id", target = "schoolInfoId")
    TransactionRecordDTO toDto(TransactionRecord transactionRecord);

    @Mapping(target = "schoolInfo.id", source = "schoolInfoId")
    TransactionRecord toEntity(TransactionRecordDTO transactionRecordDTO);

    default TransactionRecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setId(id);
        return transactionRecord;
    }

}
