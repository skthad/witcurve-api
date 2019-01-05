package com.witcurve.service.mapper;

import com.witcurve.domain.StudentStandard;
import com.witcurve.service.dto.StudentStandardDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses={StudentMapper.class, StandardMapper.class})
public interface StudentStandardMapper extends EntityMapper<StudentStandardDTO, StudentStandard>{

    StudentStandardDTO toDto(StudentStandard studentStandard);

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
