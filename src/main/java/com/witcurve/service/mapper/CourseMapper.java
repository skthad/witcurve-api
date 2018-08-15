package com.witcurve.service.mapper;

import com.witcurve.domain.Course;
import com.witcurve.domain.MasterSubject;
import com.witcurve.domain.School;
import com.witcurve.service.dto.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SchoolMapper.class})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {

    @Mapping(source = "masterSubject.id", target = "masterSubjectId")
    @Mapping(target = "schoolId", source = "school.id")
    CourseDTO toDto(Course course);

    @Mapping(target = "masterSubject", source = "masterSubjectId")
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

    default MasterSubject masterSubjectFromId(Long id) {
        if(id == null) {
            return null;
        }
        MasterSubject masterSubject = new MasterSubject();
        masterSubject.setId(id);
        return masterSubject;
    }

}
