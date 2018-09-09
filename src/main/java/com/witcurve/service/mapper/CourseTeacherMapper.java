package com.witcurve.service.mapper;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.service.dto.CourseTeacherDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClassMapper.class, CourseMapper.class, StaffMapper.class})
public interface CourseTeacherMapper extends EntityMapper<CourseTeacherDTO, CourseTeacher> {

    @Mapping(source = "standard.id", target = "standardId")
    CourseTeacherDTO toDto(CourseTeacher courseTeacher);

    @Mapping(target = "standard", source = "standardId")
    CourseTeacher toEntity(CourseTeacherDTO courseTeacherDTO);

    default CourseTeacher fromId(Long courseTeacherId) {
        if(courseTeacherId == null) {
            return null;
        }
        CourseTeacher courseTeacher = new CourseTeacher();
        courseTeacher.setId(courseTeacherId);
        return courseTeacher;
    }
}
