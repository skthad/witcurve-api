package com.witcurve.service.mapper;

import com.witcurve.domain.FeeDetails;
import com.witcurve.service.dto.FeeDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface FeeDetailsMapper extends EntityMapper<FeeDetailsDTO, FeeDetails> {


    @Mapping(source = "schoolInfoId", target = "schoolInfo.id")
    FeeDetails toEntity(FeeDetailsDTO feeDetailsDTO);

    @Mapping(target = "schoolInfoId", source = "schoolInfo.id")
    FeeDetailsDTO toDto(FeeDetails feeDetails);

    default FeeDetails fromId(Long id) {
        if (id == null) {
            return  null;
        }
        FeeDetails feeDetails = new FeeDetails();
        feeDetails.setId(id);
        return feeDetails;
    }
}
