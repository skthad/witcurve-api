package com.witcurve.service.mapper;

import com.google.common.base.Strings;
import com.witcurve.domain.Student;
import com.witcurve.domain.StudentMarks;
import com.witcurve.domain.StudentStandard;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.StudentMarksDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses={StudentMapperLite.class, EventMapper.class, ReportCardDesignMapper.class, CourseMapper.class})
public interface StudentMarksMapper extends EntityMapper<StudentMarksDTO, StudentMarks>{

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "eventDTO", source = "event")
    @Mapping(target= "reportCardDesignDTO",source= "reportCardDesign")
    @Mapping(target= "courseDTO",source= "course")
    @Mapping(target= "studentName", expression = "java(getName(studentMarks.getStudent()))")
    @Mapping(target= "standardId", expression = "java(getStandardId(studentMarks.getStudent()))")
    @Mapping(target= "grade", expression = "java(getGrade(studentMarks.getStudent()))")
    @Mapping(target= "section", expression = "java(getSection(studentMarks.getStudent()))")
    @Mapping(target= "rollNo", expression = "java(getRollNo(studentMarks.getStudent()))")
    @Mapping(target = "eventOrExamDate", expression = "java(getDate(studentMarks))")
    @Mapping(target = "examName", expression = "java(getExamName(studentMarks))")
    StudentMarksDTO toDto(StudentMarks studentMarks);

    @Mapping(source = "studentId", target = "student")
    @Mapping(source = "eventDTO", target = "event")
    @Mapping(source= "courseDTO",target= "course")
    @Mapping(source= "reportCardDesignDTO",target= "reportCardDesign")
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

    default Long getStandardId(Student student) {
        if(student.getStudentStandards() != null && student.getStudentStandards().size()!=0) {
            List<StudentStandard> studentStandards = new ArrayList<>(student.getStudentStandards());
            return studentStandards.get(0).getStandard().getId();
        }
        return null;
    }

    default Grade getGrade(Student student) {
        if(student.getStudentStandards() != null && student.getStudentStandards().size()!=0) {
            List<StudentStandard> studentStandards = new ArrayList<>(student.getStudentStandards());
            return studentStandards.get(0).getStandard().getGrade();
        }
        return null;
    }

    default String getSection(Student student) {
        if(student.getStudentStandards() != null && student.getStudentStandards().size()!=0) {
            List<StudentStandard> studentStandards = new ArrayList<>(student.getStudentStandards());
            return studentStandards.get(0).getStandard().getSection();
        }
        return null;
    }

    default String getRollNo(Student student) {
        if(student.getStudentStandards() != null && student.getStudentStandards().size()!=0) {
            List<StudentStandard> studentStandards = new ArrayList<>(student.getStudentStandards());
            return studentStandards.get(0).getRollNo();
        }
        return null;
    }

    default LocalDate getDate(StudentMarks studentMarks) {
        if(studentMarks == null) {
            return null;
        }
        if(studentMarks.getEvent()!= null) {
            if(studentMarks.getEvent().getDate() != null) {
                return studentMarks.getEvent().getDate();
            }
        } else if(studentMarks.getReportCardDesign() != null){
            if(studentMarks.getReportCardDesign().getExam()!=null) {
                return studentMarks.getReportCardDesign().getExam().getStartDate();
            }
        }
        return null;
    }

    default String getExamName(StudentMarks studentMarks) {
        if (studentMarks == null) {
            return null;
        } else {
            if(studentMarks.getReportCardDesign() == null) {
                return null;
            } else {
                if(studentMarks.getReportCardDesign().getExam() == null) {
                    return null;
                } else {
                    return studentMarks.getReportCardDesign().getExam().getName();
                }
            }
        }
    }

}
