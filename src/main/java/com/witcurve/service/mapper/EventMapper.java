package com.witcurve.service.mapper;

import com.witcurve.domain.Event;
import com.witcurve.service.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StandardMapper.class, StaffMapper.class,
    AcademicSessionMapper.class, StudentMapperLite.class, SlotCourseDetailsMapper.class,
    CourseTeacherMapper.class, SchoolInfoMapper.class})
public interface EventMapper extends EntityMapper<EventDTO, Event>{

    @Mapping(source = "academicSessionId", target = "academicSession")
    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    @Mapping(source = "standardId", target = "standard")
    @Mapping(source = "staffId", target = "staff")
    @Mapping(source = "studentId", target = "student")
    Event toEntity(EventDTO eventDTO);

    @Mapping(target = "academicSessionId", source = "academicSession.id")
    @Mapping(target = "schoolInfoId", source = "schoolInfo.id")
    @Mapping(target = "standardId", source = "standard.id")
    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "studentId", source = "student.id")
    EventDTO toDto(Event event);

    default Event fromId(Long id) {
        if (id == null) {
            return  null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
