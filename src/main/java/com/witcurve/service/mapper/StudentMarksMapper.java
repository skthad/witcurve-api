package com.witcurve.service.mapper;

import com.witcurve.domain.StudentMarks;
import com.witcurve.service.dto.StudentMarksDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={StudentMapperLite.class, EventMapper.class})
public interface StudentMarksMapper extends EntityMapper<StudentMarksDTO, StudentMarks>{

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "testId", source = "test.id")
    StudentMarksDTO toDto(StudentMarks student);

    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "testId", target = "test.id")
    StudentMarks toEntity(StudentMarksDTO studentMarksDTO);

    default StudentMarks fromId(Long id) {
        if(id == null) {
            return null;
        }
        StudentMarks studentMarks = new StudentMarks();
        studentMarks.setId(id);
        return  studentMarks;
    }
}
