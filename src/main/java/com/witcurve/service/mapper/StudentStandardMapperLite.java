package com.witcurve.service.mapper;

import com.witcurve.domain.StudentStandard;
import com.witcurve.service.dto.StudentStandardDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses={StudentMapperLite.class, StandardMapper.class})
public interface StudentStandardMapperLite extends EntityMapper<StudentStandardDTO, StudentStandard>{

    StudentStandardDTO toDto(StudentStandard student);

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
