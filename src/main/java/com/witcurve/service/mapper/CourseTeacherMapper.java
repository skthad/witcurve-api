package com.witcurve.service.mapper;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.service.dto.CourseTeacherDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseTeacherMapper {

    @Mapping(source = "standard.id", target = "classId")
    @Mapping(source = "teacher.id", target = "teacherDTO.id")
    @Mapping(source = "course.id", target = "courseDTO.id")
    CourseTeacherDTO courseTeacherToCourseTeacherDTO(CourseTeacher courseTeacher);

    @Mapping(target = "standard.id", source = "classId")
    @Mapping(target = "teacher.id", source = "teacherDTO.id")
    @Mapping(target = "course.id",  source = "courseDTO.id")
    CourseTeacher courseTeacherDTOToCourseTeacher(CourseTeacherDTO courseTeacherDTO);

    List<CourseTeacher> courseTeacherDTOsToCourseTeachers(List<CourseTeacherDTO> courseTeacherDTOS);

    List<CourseTeacherDTO> coursesTeacherToCourseTeacherDTOs(List<CourseTeacher> courseTeachers);
}
