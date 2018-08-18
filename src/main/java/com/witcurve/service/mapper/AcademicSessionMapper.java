package com.witcurve.service.mapper;

import com.witcurve.domain.AcademicSession;
import com.witcurve.domain.School;
import com.witcurve.service.dto.AcademicSessionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SchoolMapper.class})
public interface AcademicSessionMapper extends EntityMapper<AcademicSessionDTO, AcademicSession>{

    @Mapping(target = "schoolId", source = "school.id")
    AcademicSessionDTO toDto(AcademicSession academicSession);

    @Mapping(target = "school.id", source = "schoolId")
    AcademicSession toEntity(AcademicSessionDTO academicSessionDTO);

    default AcademicSession fromId(Long id) {
        if(id == null) {
            return  null;
        }
        AcademicSession academicSession = new AcademicSession();
        academicSession.setId(id);
        return academicSession;
    }


}
