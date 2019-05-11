package com.witcurve.service.mapper;

import com.witcurve.domain.MobileMetaData;
import com.witcurve.service.dto.MobileMetaDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {InstituteMapper.class })
public interface MobileMetaDataMapper extends EntityMapper<MobileMetaDataDTO, MobileMetaData>{

    @Mapping(source = "institute.id", target ="instituteId")
    MobileMetaDataDTO toDto(MobileMetaData mobileMetaData);

    @Mapping(source = "instituteId", target ="institute")
    MobileMetaData toEntity(MobileMetaDataDTO mobileMetaDataDTO);

    default MobileMetaData fromId(Long id) {
        if(id==null) {
            return null;
        }
        MobileMetaData mobileMetaData = new MobileMetaData();
        mobileMetaData.setId(id);
        return mobileMetaData;
    }
}
