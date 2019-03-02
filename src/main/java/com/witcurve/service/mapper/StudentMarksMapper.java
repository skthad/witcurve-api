package com.witcurve.service.mapper;

import com.google.common.base.Strings;
import com.witcurve.domain.Student;
import com.witcurve.domain.StudentMarks;
import com.witcurve.domain.StudentStandard;
import com.witcurve.service.dto.StudentMarksDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses={StudentMapperLite.class, EventMapper.class, ExamCourseDetailsMapper.class})
public interface StudentMarksMapper extends EntityMapper<StudentMarksDTO, StudentMarks>{

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "eventDTO", source = "event")
    @Mapping(target= "examCourseDetailsDTO",source= "examCourseDetails")
    @Mapping(target= "studentName", expression = "java(getName(studentMarks.getStudent()))")
    @Mapping(target= "rollNo", expression = "java(getRollNo(studentMarks.getStudent()))")
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

    default String getName(Student student) {
        String name = student.getFirstName();
        if(!Strings.isNullOrEmpty(student.getMiddleName())) {
            name +=" "+student.getMiddleName();
        }
        name += " "+ student.getLastName();

        return name;
    }

    default String getRollNo(Student student) {
        if(student.getStudentStandards() != null && student.getStudentStandards().size()!=0) {
            List<StudentStandard> studentStandards = new ArrayList<>(student.getStudentStandards());
            return studentStandards.get(0).getRollNo();
        }
        return null;
    }

}
