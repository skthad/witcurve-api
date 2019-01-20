package com.witcurve.service.mapper;

import com.witcurve.domain.SchoolInfo;
import com.witcurve.service.dto.SchoolInfoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses={SchoolMapper.class})
public interface SchoolInfoMapper extends EntityMapper<SchoolInfoDTO, SchoolInfo> {

    SchoolInfoDTO toDto(SchoolInfo schoolInfo);

    SchoolInfo toEntity(SchoolInfoDTO schoolInfoDTO);

}
