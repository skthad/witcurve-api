package com.witcurve.service.mapper;

import com.witcurve.domain.Guardian;
import com.witcurve.domain.Student;
import com.witcurve.service.dto.GuardianDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public interface GuardianMapper extends EntityMapper<GuardianDTO, Guardian>{

    @Mapping(source = "studentId", target = "student.id")
    Guardian toEntity(GuardianDTO guardianDTO);

    @Mapping(target = "studentId", source = "student.id")
    GuardianDTO toDto(Guardian guardian);

    default Guardian fromId(Long id) {
        if (id == null) {
            return  null;
        }
        Guardian guardian = new Guardian();
        guardian.setId(id);
        return guardian;
    }

}
