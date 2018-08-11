package com.witcurve.service.mapper;

import com.witcurve.domain.AcademicSession;
import com.witcurve.service.dto.AcademicSessionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AcademicSessionMapper {

    @Mapping(target = "schoolId", source = "school.id")
    AcademicSessionDTO academicSessionToAcademicSessionDTO(AcademicSession academicSession);

    @Mapping(target = "school.id", source = "schoolId")
    AcademicSession academicSessionDTOToAcademicSession(AcademicSessionDTO academicSessionDTO);

    List<AcademicSessionDTO> academicSessionsToAcademicSessionDTOs(List<AcademicSession> academicSessions);

    List<AcademicSession> academicSessionDTOsToAcademicSessions(List<AcademicSessionDTO> academicSessionDTOS);


}
