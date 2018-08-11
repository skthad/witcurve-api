package com.witcurve.service;

import com.witcurve.service.dto.EventDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    EventDTO saveOrUpdate(EventDTO eventDTO) throws WitcurveException;

    EventDTO getEventById(Long eventId) throws WitcurveException;

    void deleteEvent(Long eventId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenDate(LocalDate eventDate, Long schoolId);
}
