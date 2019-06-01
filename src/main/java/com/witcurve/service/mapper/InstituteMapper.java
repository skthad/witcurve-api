package com.witcurve.service.mapper;

import com.witcurve.domain.Institute;
import com.witcurve.service.dto.InstituteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstituteMapper extends EntityMapper<InstituteDTO, Institute> {

    @Mapping(ignore = true, target = "schoolMap")
    InstituteDTO toDto(Institute institute);

    Institute toEntity(InstituteDTO instituteDTO);

    default Institute fromId(Long id) {
        if(id == null) {
            return null;
        }
        Institute institute = new Institute();
        institute.setId(id);
        return institute;
    }


}
