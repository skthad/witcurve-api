package com.witcurve.service.impl;

import com.witcurve.domain.Event;
import com.witcurve.repository.EventRepository;
import com.witcurve.service.EventService;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.mapper.EventMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final Logger log  = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventMapper eventMapper;


    @Override
    public EventDTO saveOrUpdate(EventDTO eventDTO) throws WitcurveException {
        log.debug("Request to save or update eventDTO : {}", eventDTO);
        if (eventDTO.getEventDate() == null && (eventDTO.getFromDate() == null || eventDTO.getToDate() == null)) {
            throw new WitcurveException("Event should have event date or from date and to date");
        }
        Event event = eventMapper.eventDTOToEvent(eventDTO);
        event = eventRepository.save(event);
        return eventMapper.eventToEventDTO(event);
    }

    @Override
    public EventDTO getEventById(Long eventId) throws WitcurveException {
        log.debug("Request to get event with id : {}", eventId);
        Event event = eventRepository.findById(eventId).get();

        if (event ==  null) {
            throw new WitcurveException("No Event with given id");
        }
        return eventMapper.eventToEventDTO(event);
    }

    @Override
    public void deleteEvent(Long eventId) throws WitcurveException {
        log.debug("Request to delete event with id : {}", eventId);
        Event event = eventRepository.findById(eventId).get();

        if (event == null){
            throw new WitcurveException("No Event with given id");
        }
        eventRepository.delete(event);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenDate(LocalDate eventDate, Long schoolId) {
        log.debug("Request to get tests with eventDate : {} and schoolId : {}", eventDate, schoolId);
        List<Event> eventsOnGivenDate = eventRepository.findEventsByEventDateAndSchoolId(eventDate, schoolId);
        return eventMapper.eventsToEventDTOs(eventsOnGivenDate);
    }
}
