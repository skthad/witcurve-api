package com.witcurve.service.mapper;

import com.witcurve.domain.FeePaymentDetail;
import com.witcurve.service.dto.FeePaymentDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FeeDetailsMapper.class})
public interface FeePaymentDetailMapper extends EntityMapper<FeePaymentDetailDTO, FeePaymentDetail> {


    @Mapping(source = "feeTypeId", target = "feeType.id")
    @Mapping(source = "feeDescriptionId", target = "feeDescription.id")
    FeePaymentDetail toEntity(FeePaymentDetailDTO feePaymentDetailDTO);

    @Mapping(target = "feeTypeId", source = "feeType.id")
    @Mapping(target = "feeDescriptionId", source = "feeDescription.id")
    FeePaymentDetailDTO toDto(FeePaymentDetail feePaymentDetail);

    default FeePaymentDetail fromId(Long id) {
        if (id == null) {
            return null;
        }
        FeePaymentDetail feePaymentDetail = new FeePaymentDetail();
        feePaymentDetail.setId(id);
        return feePaymentDetail;
    }
}

