package com.witcurve.service.mapper;

import com.witcurve.domain.Standard;
import com.witcurve.service.dto.StandardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StaffMapper.class, TermMapper.class})
public interface StandardMapper extends EntityMapper<StandardDTO, Standard> {

    @Mapping(target = "classTeacherId", source = "classTeacher.id")
    StandardDTO toDto(Standard standard);

    @Mapping(target = "classTeacher", source = "classTeacherId")
    Standard toEntity(StandardDTO standardDTO);

    default Standard fromId(Long id) {
        if(id == null) {
            return null;
        }
        Standard standard =  new Standard();
        standard.setId(id);
        return standard;

    }

}
