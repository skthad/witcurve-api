package com.witcurve.service.mapper;

import com.google.common.base.Strings;
import com.witcurve.domain.CourseGrade;
import com.witcurve.domain.Student;
import com.witcurve.domain.StudentStandard;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.CourseGradeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentMapperLite.class, ReportCardDesignMapper.class, CourseMapper.class})
public interface CourseGradeMapper extends EntityMapper<CourseGradeDTO, CourseGrade> {

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "reportCardDesignDTO", source = "reportCardDesign")
    @Mapping(target = "courseDTO", source = "course")
    @Mapping(target = "studentName", expression = "java(getName(courseGrade.getStudent()))")
    @Mapping(target = "standardId", expression = "java(getStandardId(courseGrade.getStudent()))")
    @Mapping(target = "grade", expression = "java(getGrade(courseGrade.getStudent()))")
    @Mapping(target = "section", expression = "java(getSection(courseGrade.getStudent()))")
    @Mapping(target = "rollNo", expression = "java(getRollNo(courseGrade.getStudent()))")
    @Mapping(target = "examDate", expression = "java(getDate(courseGrade))")
    @Mapping(target = "examName", expression = "java(getExamName(courseGrade))")
    CourseGradeDTO toDto(CourseGrade courseGrade);

    @Mapping(source = "studentId", target = "student")
    @Mapping(source = "courseDTO", target = "course")
    @Mapping(source = "reportCardDesignDTO", target = "reportCardDesign")
    CourseGrade toEntity(CourseGradeDTO courseGradeDTO);

    default CourseGrade fromId(Long id) {
        if (id == null) {
            return null;
        }
        CourseGrade courseGrade = new CourseGrade();
        courseGrade.setId(id);
        return courseGrade;
    }

    default String getName(Student student) {
        String name = student.getFirstName();
        if (!Strings.isNullOrEmpty(student.getMiddleName())) {
            name += " " + student.getMiddleName();
        }
        name += " " + student.getLastName();

        return name;
    }

    default Long getStandardId(Student student) {
        if (student.getStudentStandards() != null && student.getStudentStandards().size() != 0) {
            List<StudentStandard> studentStandards = new ArrayList<>(student.getStudentStandards());
            return studentStandards.get(0).getStandard().getId();
        }
        return null;
    }

    default Grade getGrade(Student student) {
        if (student.getStudentStandards() != null && student.getStudentStandards().size() != 0) {
            List<StudentStandard> studentStandards = new ArrayList<>(student.getStudentStandards());
            return studentStandards.get(0).getStandard().getGrade();
        }
        return null;
    }

    default String getSection(Student student) {
        if (student.getStudentStandards() != null && student.getStudentStandards().size() != 0) {
            List<StudentStandard> studentStandards = new ArrayList<>(student.getStudentStandards());
            return studentStandards.get(0).getStandard().getSection();
        }
        return null;
    }

    default String getRollNo(Student student) {
        if (student.getStudentStandards() != null && student.getStudentStandards().size() != 0) {
            List<StudentStandard> studentStandards = new ArrayList<>(student.getStudentStandards());
            return studentStandards.get(0).getRollNo();
        }
        return null;
    }

    default LocalDate getDate(CourseGrade courseGrade) {
        if (courseGrade == null) {
            return null;
        }
        if (courseGrade.getReportCardDesign() != null) {
            if (courseGrade.getReportCardDesign().getExam() != null) {
                return courseGrade.getReportCardDesign().getExam().getStartDate();
            }
        }
        return null;
    }

    default String getExamName(CourseGrade courseGrade) {
        if (courseGrade == null) {
            return null;
        } else {
            if (courseGrade.getReportCardDesign() == null) {
                return null;
            } else {
                if (courseGrade.getReportCardDesign().getExam() == null) {
                    return null;
                } else {
                    return courseGrade.getReportCardDesign().getExam().getName();
                }
            }
        }
    }
}
