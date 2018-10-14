package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.Event;
import com.witcurve.domain.enumeration.View;
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
     * creates events
     * @param eventDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/event")
    @Timed
    public ResponseEntity<List<EventDTO>> createEvent(@RequestBody List<EventDTO> eventDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Events : {}",eventDTOs);
        for(EventDTO eventDTO: eventDTOs) {
            if (eventDTO.getId() != null) {
                throw new WitcurveException("New Event can't already have an id");
            }
        }
        List<EventDTO> result = eventService.saveOrUpdate(eventDTOs);
        return ResponseEntity.ok(result);
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
     * update given events
     * @param eventDTOs
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/event")
    @Timed
    public ResponseEntity<List<EventDTO>> updateEvent(@RequestBody List<EventDTO> eventDTOs) throws WitcurveException {
        log.debug("Request to update events : {}",eventDTOs);
        for(EventDTO eventDTO : eventDTOs) {
            if (eventDTO.getId() == null) {
                throw new WitcurveException("Id is required for update request");
            }
        }
        List<EventDTO> result = eventService.saveOrUpdate(eventDTOs);
        return ResponseEntity.ok(result);
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
        @PathVariable Long studentId) throws WitcurveException, URISyntaxException{
        log.debug("Request to get events on given date : {} for student with id : {}", eventDate, studentId);

        //add null checks later and change log statement
        List<EventDTO> result = new ArrayList<>();
        if(type.equals(ViewType.DAY)) {
            if(eventDate == null) {
                throw new WitcurveException("There should be eventDate param for DAY view");
            }
            LocalDate date = getLocalDate(eventDate);
            result = eventService.findAllEventsOnGivenDateForStudent(date, studentId);
        }
        if(type.equals(ViewType.MONTH)) {
            if(month == null || year ==null) {
                throw new WitcurveException("There should be month and year param for MONTH view");
            }
            result = eventService.findAllEventsOnGivenMonthForStudent(month, year, studentId);
        }
        if(type.equals(ViewType.DIARY)) {
            if(eventDate == null) {
                throw new WitcurveException("There should be eventDate param for DIARY view");
            }
            LocalDate date = getLocalDate(eventDate);
            result = eventService.findAllEventsForDiary(date, studentId);
        }
        if(type.equals(ViewType.ANNOUNCEMENT)) {
            if (eventDate == null) {
                throw new WitcurveException("There should be eventDate param for ANNOUNCEMENTS view");
            }
            LocalDate date = getLocalDate(eventDate);
            result = eventService.findAllEventsForAnnouncements(date, studentId);
        }

            return new ResponseEntity<>(result,  HttpStatus.OK);
    }

    /**
     *
     *
     * @param month
     * @param studentId
     * @return
     */
    @GetMapping("/event/student/{studentId}/dates")
    @Timed
    public ResponseEntity<List<LocalDate>> getAllEventsDatesForStudentInAMonth(
        @RequestParam(value = "month", required = false) Integer month,
        @RequestParam(value = "year", required = false) Integer year,
        @PathVariable Long studentId) throws WitcurveException, URISyntaxException{
        if(month == null || year ==null) {
            throw new WitcurveException("There should be month and year param for MONTH view");
        }
        List<LocalDate> result = eventService.findAllEventDatesOnGivenMonthForStudent(month, year, studentId);


        return new ResponseEntity<>(result,  HttpStatus.OK);
    }


    /**
     *
     * @param eventDate
     * @param standardId
     * @return
     */
    @GetMapping("/event/standard/{standardId}")
    @Timed
    public ResponseEntity<List<EventDTO>> getAllEventsForStandard(
        @RequestParam(value = "eventDate", required = false) String eventDate,
        @PathVariable Long standardId) throws WitcurveException, URISyntaxException{
        log.debug("Request to get events on given date : {} for standard with id : {}", eventDate, standardId);
        LocalDate date = getLocalDate(eventDate);
        List<EventDTO> result = eventService.findAllEventsOnGivenDateForStandard(date, standardId);
        return new ResponseEntity<>(result,  HttpStatus.OK);
    }


}
