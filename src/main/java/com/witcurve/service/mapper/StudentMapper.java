package com.witcurve.service.mapper;

import com.witcurve.domain.Student;
import com.witcurve.service.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StandardMapper.class, UserMapper.class})
public interface StudentMapper extends EntityMapper<StudentDTO, Student>{

    @Mapping(target = "schoolId", source = "school.id")
    StudentDTO toDto(Student student);

    @Mapping(source = "schoolId", target = "school.id")
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
