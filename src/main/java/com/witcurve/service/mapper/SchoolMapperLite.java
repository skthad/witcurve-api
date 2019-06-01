package com.witcurve.service.mapper;

import com.witcurve.domain.School;
import com.witcurve.service.dto.SchoolDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SchoolMapperLite extends EntityMapper<SchoolDTO, School> {

    default SchoolDTO toDto(School school) {
        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setId(school.getId());
        schoolDTO.setName(school.getName());
        return schoolDTO;
    }

}
