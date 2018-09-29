package com.witcurve.service.mapper;

import com.witcurve.domain.School;
import com.witcurve.service.dto.SchoolDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {InstituteMapper.class})
public interface SchoolMapper extends EntityMapper<SchoolDTO, School> {

    @Mapping(target = "instituteId", source = "institute.id")
    SchoolDTO toDto(School school);

    @Mapping(target = "institute.id", source = "instituteId")
    School toEntity(SchoolDTO schoolDTO);

    default School fromId(Long id) {
        if(id == null) {
            return null;
        }
        School school = new School();
        school.setId(id);
        return school;
    }


}
