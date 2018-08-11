package com.witcurve.service.mapper;

import com.witcurve.domain.Class;
import com.witcurve.service.dto.ClassDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {StaffMapper.class})
public interface ClassMapper {

    @Mapping(target = "termId", source = "term.id")
    @Mapping(target = "schoolId", source = "school.id")
    ClassDTO classToClassDTO(Class aclass);

    @Mapping(target = "term.id", source = "termId")
    @Mapping(target = "school.id", source = "schoolId")
    Class classDTOToClass(ClassDTO classDTO);

    List<ClassDTO> classesToClassDTOs(List<Class> classes);

    List<Class> classDTOsToClasses(List<ClassDTO> classDTOS);
}
