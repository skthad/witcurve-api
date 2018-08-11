package com.witcurve.service.mapper;

import com.witcurve.domain.School;
import com.witcurve.service.dto.SchoolDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SchoolMapper {

    SchoolDTO schoolToSchoolDTO(School school);

    School schoolDTOToSchool(SchoolDTO schoolDTO);

    List<SchoolDTO> schoolsToSchoolsDTO(List<School> schools);

    List<School> schoolDTOsToSchool(List<SchoolDTO> schoolDTOS);


}
