package com.witcurve.service.mapper;

import com.witcurve.domain.SchoolInfo;
import com.witcurve.service.dto.SchoolInfoDTO;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface SchoolInfoMapperLite extends EntityMapper<SchoolInfoDTO, SchoolInfo> {

    default SchoolInfoDTO toDto(SchoolInfo schoolInfo) {
        SchoolInfoDTO schoolInfoDTO = new SchoolInfoDTO();
        schoolInfoDTO.setId(schoolInfo.getId());
        schoolInfoDTO.setBoard(schoolInfo.getBoard());
        schoolInfoDTO.setMedium(schoolInfo.getMedium());
        schoolInfoDTO.setPrimaryBoard(schoolInfo.getPrimaryBoard());
        return schoolInfoDTO;
    }
}
