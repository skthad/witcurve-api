package com.witcurve.service.mapper;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.service.dto.CourseTeacherDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StandardMapper.class, CourseMapper.class, StaffMapper.class})
public interface CourseTeacherMapper extends EntityMapper<CourseTeacherDTO, CourseTeacher> {

    CourseTeacherDTO toDto(CourseTeacher courseTeacher);

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
