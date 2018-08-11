package com.witcurve.service.mapper;

import com.witcurve.domain.Student;
import com.witcurve.service.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "schoolId", source = "school.id")
    @Mapping(target = "classId", source = "standard.id")
    StudentDTO studentToStudentDTO(Student student);

    @Mapping(source = "schoolId", target = "school.id")
    @Mapping(source = "classId", target = "standard.id")
    Student studentDTOToStudent(StudentDTO studentDTO);

    List<StudentDTO> studentsToStudentDTOs(List<Student> students);

    List<Student> studentDTOsToStudent(List<StudentDTO> studentDTOS);
}
