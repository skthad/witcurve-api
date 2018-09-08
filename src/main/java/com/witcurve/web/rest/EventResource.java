package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.ViewType;
import com.witcurve.service.EventService;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.witcurve.service.util.WitcurveUtil.getLocalDate;

@RestController
@RequestMapping("/api")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    @Autowired
    EventService eventService;

    /**
     * creates a event
     * @param eventDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/event")
    @Timed
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Event");
        if (eventDTO.getId() != null) {
            throw new WitcurveException("New Event can't already have an id");
        }
        EventDTO result = eventService.saveOrUpdate(eventDTO);
        return ResponseEntity.created(new URI("/api/event/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("event", result.getId().toString()))
            .body(result);
    }

    /**
     * get event by id
     * @param eventId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/event/{eventId}")
    @Timed
    public ResponseEntity<EventDTO> getEventById(@PathVariable("eventId") Long eventId) throws WitcurveException {
        log.debug("Request to get Event with id {}", eventId);
        EventDTO result = eventService.getEventById(eventId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given event
     * @param eventDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/event")
    @Timed
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO) throws WitcurveException {
        log.debug("Request to update event");
        if (eventDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        EventDTO result = eventService.saveOrUpdate(eventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("event", eventDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the event
     * @param eventId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/event/{eventId}")
    @Timed
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) throws WitcurveException {
        log.debug("REST request to delete Event: {}", eventId);
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("An event is deleted with identifier " + eventId,
            eventId.toString())).build();
    }

    /**
     *
     * @param eventDate
     * @param month
     * @param studentId
     * @return
     */
    @GetMapping("/event/student/{studentId}")
    @Timed
    public ResponseEntity<List<EventDTO>> getAllEventsForStudent(
        @RequestParam(value = "eventDate", required = false) String eventDate,
        @RequestParam(value = "month", required = false) Integer month,
        @RequestParam(value = "year", required = false) Integer year,
        @RequestParam(value = "type") ViewType type,
        @PathVariable Long studentId) {
        log.debug("Request to get events on given date : {} for student with id : {}", eventDate, studentId);

        //add null checks later and change log statement
        List<EventDTO> result = new ArrayList<>();
        if(type.equals(ViewType.DAY)) {
            if(eventDate == null) {
                //throw error
            }
            LocalDate date = getLocalDate(eventDate);
            result = eventService.findAllEventsOnGivenDateForStudent(date, studentId);
        }
        if(type.equals(ViewType.MONTH)) {
            if(month == null || year ==null) {
               // throw error
            }
            result = eventService.findAllEventsOnGivenMonthForStudent(month, year, studentId);
        }

        return new ResponseEntity<>(result,  HttpStatus.OK);
    }


}
