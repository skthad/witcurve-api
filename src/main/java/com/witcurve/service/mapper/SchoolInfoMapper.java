package com.witcurve.service.mapper;

import com.witcurve.domain.SchoolInfo;
import com.witcurve.service.dto.SchoolInfoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses={SchoolMapper.class})
public interface SchoolInfoMapper extends EntityMapper<SchoolInfoDTO, SchoolInfo> {

    SchoolInfoDTO toDto(SchoolInfo schoolInfo);

    SchoolInfo toEntity(SchoolInfoDTO schoolInfoDTO);

    default SchoolInfo fromId(Long id) {
        if (id == null) {
            return  null;
        }
        SchoolInfo schoolInfo = new SchoolInfo();
        schoolInfo.setId(id);
        return schoolInfo;
    }

}
