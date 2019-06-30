package com.witcurve.service.mapper;

import com.witcurve.domain.SchoolInfo;
import com.witcurve.service.dto.SchoolInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={SchoolMapper.class})
public interface SchoolInfoMapper extends EntityMapper<SchoolInfoDTO, SchoolInfo> {

    @Mapping(target = "mainSchoolInfoUserId", ignore = true)
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
