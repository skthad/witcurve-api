package com.witcurve.service.mapper;

import com.witcurve.domain.Class;
import com.witcurve.service.dto.ClassDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {StaffMapper.class, TermMapper.class, SchoolMapper.class})
public interface ClassMapper extends EntityMapper<ClassDTO, Class> {

    @Mapping(target = "termId", source = "term.id")
    @Mapping(target = "schoolId", source = "school.id")
    ClassDTO toDto(Class aclass);

    @Mapping(target = "term.id", source = "termId")
    @Mapping(target = "school.id", source = "schoolId")
    Class toEntity(ClassDTO classDTO);

    default Class fromId(Long id) {
        if(id == null) {
            return null;
        }
        Class standard =  new Class();
        standard.setId(id);
        return standard;

    }
}
