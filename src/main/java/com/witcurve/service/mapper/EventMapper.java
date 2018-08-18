package com.witcurve.service.mapper;

import com.witcurve.domain.Event;
import com.witcurve.service.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClassMapper.class, CourseMapper.class, AcademicSessionMapper.class, StudentMapper.class, ExamMapper.class})
public interface EventMapper extends EntityMapper<EventDTO, Event>{

    @Mapping(source = "academicSessionId", target = "academicSession")
    @Mapping(source = "standardId", target = "standard")
    @Mapping(source = "courseId", target = "course")
    @Mapping(source = "studentId", target = "student")
    @Mapping(source = "examId", target = "exam")
    Event toEntity(EventDTO eventDTO);

    @Mapping(target = "academicSessionId", source = "academicSession.id")
    @Mapping(target = "standardId", source = "standard.id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "examId", source = "exam.id")
    EventDTO toDto(Event event);
}
