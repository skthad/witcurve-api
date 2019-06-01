package com.witcurve.service.mapper;

import com.witcurve.domain.StudentStandard;
import com.witcurve.service.dto.StudentStandardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={StudentMapperLite.class, StandardMapper.class, AcademicSessionMapper.class})
public interface StudentStandardMapper extends EntityMapper<StudentStandardDTO, StudentStandard>{

    @Mapping(source = "session.id", target = "sessionId")
    StudentStandardDTO toDto(StudentStandard studentStandard);

    @Mapping(source = "sessionId", target = "session")
    StudentStandard toEntity(StudentStandardDTO studentStandardDTO);

    default StudentStandard fromId(Long id) {
        if(id == null) {
            return null;
        }
        StudentStandard studentStandard = new StudentStandard();
        studentStandard.setId(id);
        return  studentStandard;
    }
}
