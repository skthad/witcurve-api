package com.witcurve.service.mapper;

import com.witcurve.domain.Event;
import com.witcurve.service.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "academicSessionId", target = "academicSession.id")
    Event eventDTOToEvent(EventDTO eventDTO);

    @Mapping(target = "academicSessionId", source = "academicSession.id")
    EventDTO eventToEventDTO(Event event);

    List<Event> eventDTOsToEvents(List<EventDTO> eventDTOS);

    List<EventDTO> eventsToEventDTOs(List<Event> events);
}
