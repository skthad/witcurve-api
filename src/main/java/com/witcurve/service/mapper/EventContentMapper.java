package com.witcurve.service.mapper;

import com.witcurve.domain.Event;
import com.witcurve.domain.EventContent;
import com.witcurve.domain.ExamCourseDetails;
import com.witcurve.service.dto.EventContentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CourseContentMapper.class})
public interface EventContentMapper extends EntityMapper<EventContentDTO, EventContent> {

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "ecdId", source = "ecd.id")
    EventContentDTO toDto(EventContent courseContent);

    @Mapping(target = "event", source = "eventId")
    @Mapping(target = "ecd", source = "ecdId")
    EventContent toEntity(EventContentDTO courseContentDTO);

    default Event eventFromId (Long id) {
        if(id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return  event;
    }

    default ExamCourseDetails ecdFromId (Long id) {
        if(id == null) {
            return null;
        }
        ExamCourseDetails ecd = new ExamCourseDetails();
        ecd.setId(id);
        return  ecd;
    }

}
