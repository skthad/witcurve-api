package com.witcurve.service.mapper;

import com.witcurve.domain.StudentMarks;
import com.witcurve.service.dto.StudentMarksDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={StudentMapperLite.class, EventMapper.class, ExamCourseDetailsMapper.class})
public interface StudentMarksMapper extends EntityMapper<StudentMarksDTO, StudentMarks>{

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "eventDTO", source = "event")
    @Mapping(target= "examCourseDetailsDTO",source= "examCourseDetails")
    StudentMarksDTO toDto(StudentMarks studentMarks);

    @Mapping(source = "studentId", target = "student")
    @Mapping(source = "eventDTO", target = "event")
    @Mapping(source="examCourseDetailsDTO", target="examCourseDetails")
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
