package com.witcurve.service.mapper;

import com.witcurve.domain.Course;
import com.witcurve.domain.MasterSubject;
import com.witcurve.service.dto.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SchoolMapper.class})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {

    @Mapping(source = "masterSubject.name", target = "masterSubject")
    @Mapping(target = "schoolId", source = "school.id")
    CourseDTO toDto(Course course);

    @Mapping(target = "masterSubject.name", source = "masterSubject")
    @Mapping(source = "schoolId", target = "school")
    Course toEntity(CourseDTO courseDTO);

    default Course fromId(Long id) {
        if(id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }

    default MasterSubject masterSubjectFromName(String name) {
        if(name == null) {
            return null;
        }
        MasterSubject masterSubject = new MasterSubject();
        masterSubject.setName(name);
        return masterSubject;
    }

}
