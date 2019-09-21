package com.witcurve.service.mapper;

import com.witcurve.domain.StudentCourse;
import com.witcurve.service.dto.StudentCourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CourseMapper.class, StudentStandardMapper.class})
public interface StudentCourseMapper extends EntityMapper<StudentCourseDTO, StudentCourse>{

    @Mapping(source = "studentStandard.id", target = "studentStandardId")
    @Mapping(source = "course.id", target = "courseId")
    StudentCourseDTO toDto(StudentCourse studentCourse);

    @Mapping(target = "studentStandard", source = "studentStandardId")
    @Mapping(target = "course", source = "courseId")
    StudentCourse toEntity(StudentCourseDTO studentCourseDTO);

    default StudentCourse fromId(Long id) {
        if(id == null) {
            return null;
        }
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setId(id);
        return studentCourse;
    }

}
