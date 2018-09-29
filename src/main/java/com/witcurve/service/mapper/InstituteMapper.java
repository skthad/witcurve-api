package com.witcurve.service.mapper;

import com.witcurve.domain.Institute;
import com.witcurve.service.dto.InstituteDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstituteMapper extends EntityMapper<InstituteDTO, Institute> {

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
