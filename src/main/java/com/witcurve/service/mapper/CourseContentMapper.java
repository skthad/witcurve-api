package com.witcurve.service.mapper;

import com.witcurve.domain.Course;
import com.witcurve.domain.CourseContent;
import com.witcurve.service.dto.CourseContentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseContentMapper extends EntityMapper<CourseContentDTO, CourseContent> {

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "parentContentId", source = "parentContent.id")
    CourseContentDTO toDto(CourseContent courseContent);

    @Mapping(target = "course", source = "courseId")
    @Mapping(target = "parentContent", source = "parentContentId")
    CourseContent toEntity(CourseContentDTO courseContentDTO);

    default Course courseFromId(Long id) {
        if(id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }

    default CourseContent courseContentFromId(Long id) {
        if(id == null) {
            return null;
        }
        CourseContent courseContent = new CourseContent();
        courseContent.setId(id);
        return courseContent;
    }

}
