package com.witcurve.service.mapper;

import com.witcurve.domain.School;
import com.witcurve.service.dto.SchoolDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SchoolMapper extends EntityMapper<SchoolDTO, School> {

    SchoolDTO toDto(School school);

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
