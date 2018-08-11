package com.witcurve.service.mapper;

import com.witcurve.domain.Guardian;
import com.witcurve.service.dto.GuardianDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GuardianMapper {

    @Mapping(source = "studentId", target = "student.id")
    Guardian guardianDTOToGuardian(GuardianDTO guardianDTO);

    @Mapping(target = "studentId", source = "student.id")
    GuardianDTO guardianToGuardianDTO(Guardian guardian);

    List<Guardian> guardianDTOsToGuardians(List<GuardianDTO> guardianDTOS);

    List<GuardianDTO> guardiansToGuardians(List<Guardian> guardians);
}
