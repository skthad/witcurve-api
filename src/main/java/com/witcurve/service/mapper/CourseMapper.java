package com.witcurve.service.mapper;

import com.witcurve.domain.Course;
import com.witcurve.service.dto.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "masterSubject.id", target = "masterSubjectId")
    @Mapping(target = "schoolId", source = "school.id")
    CourseDTO courseToCourseDTO(Course course);

    @Mapping(target = "masterSubject.id", source = "masterSubjectId")
    @Mapping(source = "schoolId", target = "school.id")
    Course courseDTOToCourse(CourseDTO courseDTO);

    List<CourseDTO> coursesToCourseDTOs(List<Course> courses);

    List<Course> courseDTOsToCourses(List<Course> courses);
}
