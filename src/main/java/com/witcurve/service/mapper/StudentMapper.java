package com.witcurve.service.mapper;

import com.witcurve.domain.Student;
import com.witcurve.service.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, SchoolInfoMapper.class})
public interface StudentMapper extends EntityMapper<StudentDTO, Student>{

    @Mapping(target = "user", ignore = true)
    StudentDTO toDto(Student student);

    @Mapping(target = "user", ignore = true)
    Student toEntity(StudentDTO studentDTO);

    default Student fromId(Long id) {
        if(id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return  student;
    }
}
