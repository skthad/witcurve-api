package com.witcurve.service.mapper;

import com.witcurve.domain.StudentStandard;
import com.witcurve.service.dto.StudentStandardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={StudentMapper.class})
public interface StudentStandardMapper extends EntityMapper<StudentStandardDTO, StudentStandard>{

    @Mapping(target = "standardId", source = "standard.id")
    StudentStandardDTO toDto(StudentStandard student);

    @Mapping(source = "standardId", target = "standard.id")
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
