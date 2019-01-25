package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.ViewType;
import com.witcurve.service.EventService;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<List<EventDTO>> createEvents(@RequestBody List<EventDTO> eventDTOs) throws WitcurveException, URISyntaxException {
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

    @GetMapping("/event/attendance")
    @Timed
    public ResponseEntity<List<EventDTO>> getAttendance(@RequestParam(value = "fromDate") LocalDate fromDate,
                                                        @RequestParam(value = "toDate") LocalDate toDate,
                                                  @RequestParam(value = "studentId", required = false) Long studentId,
                                                  @RequestParam(value = "standardId", required = false) Long standardId,
                                                  @RequestParam(value = "staffId", required = false) Long staffId,
                                                @RequestParam(value = "schoolInfoId", required = false) Long schoolInfoId) throws WitcurveException {
        log.debug("Request to get attendance");
        List<EventDTO> result = eventService.getAttendance(fromDate, toDate, studentId, standardId, staffId, schoolInfoId);
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
        try {
            eventService.deleteEvent(eventId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("An event is deleted with identifier " + eventId,
                eventId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

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
        @RequestParam(value = "eventDate", required = false) LocalDate eventDate,
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
            result = eventService.findAllEventsOnGivenDateForStudent(eventDate, studentId);
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
            result = eventService.findAllEventsForDiary(eventDate, studentId);
        }
        if(type.equals(ViewType.UPCOMING_EVENTS)) {
            if (eventDate == null) {
                throw new WitcurveException("There should be eventDate param for ANNOUNCEMENTS view");
            }
            result = eventService.findEventsByDateRangeForStudentInUpcomingEvents(eventDate, studentId);
        }
//        if(type.equals(ViewType.LEAVE)) {
//            result = eventService.getAllLeavesForStudent(eventDate, studentId);
//        }

            return new ResponseEntity<>(result,  HttpStatus.OK);
    }

    /**
     *
     * @param eventDate
     * @param month
     * @param staffId
     * @return
     */
    @GetMapping("/event/staff/{staffId}")
    @Timed
    public ResponseEntity<List<EventDTO>> getAllEventsForTeacher(
        @RequestParam(value = "eventDate", required = false) LocalDate eventDate,
        @RequestParam(value = "month", required = false) Integer month,
        @RequestParam(value = "year", required = false) Integer year,
        @RequestParam(value = "type") ViewType type,
        @PathVariable Long staffId) throws WitcurveException, URISyntaxException{
        log.debug("Request to get events for type : {} for staff with id : {}", type, staffId);

        //add null checks later and change log statement
        List<EventDTO> result = new ArrayList<>();
        if(type.equals(ViewType.DAY)) {
            if(eventDate == null) {
                throw new WitcurveException("There should be eventDate param for DAY view");
            }
            //LocalDate date = getLocalDate(eventDate);

            //TODO: no need to send termId anymore, since it is based on date
            result = eventService.findAllEventsOnGivenDateForStaff(eventDate, staffId);
        }
        if(ViewType.TEST.equals(type)){
            if(eventDate == null) {
                throw new WitcurveException("There should be eventDate param for TEST view");
            }
            result=eventService.findAllTestAndAssignmentByTeacherInWeek(staffId,eventDate,ViewType.TEST);
        }
        if(ViewType.ASSIGNMENT.equals(type)){
            if(eventDate == null) {
                throw new WitcurveException("There should be eventDate param for ASSIGNMENT view");
            }
            result=eventService.findAllTestAndAssignmentByTeacherInWeek(staffId,eventDate,ViewType.ASSIGNMENT);
        } else if(type.equals(ViewType.UPCOMING_EVENTS)) {
            if (eventDate == null) {
                throw new WitcurveException("There should be eventDate param for ANNOUNCEMENTS view");
            }
            result = eventService.findEventsByDateRangeForStaffInUpcomingEvents(eventDate, staffId);
        }
//        if(type.equals(ViewType.MONTH)) {
//            if(month == null || year ==null) {
//                throw new WitcurveException("There should be month and year param for MONTH view");
//            }
//            result = eventService.findAllEventsOnGivenMonthForStudent(month, year, studentId);
//        }
//        if(type.equals(ViewType.DIARY)) {
//            if(eventDate == null) {
//                throw new WitcurveException("There should be eventDate param for DIARY view");
//            }
//            LocalDate date = getLocalDate(eventDate);
//            result = eventService.findAllEventsForDiary(date, studentId);
//        }
//        if(type.equals(ViewType.LEAVE)) {
//            result = eventService.getAllLeavesForStudent(eventDate == null ? null : getLocalDate(eventDate), studentId);
//        }

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
        @RequestParam(value = "eventDate", required = false) LocalDate eventDate,
        @RequestParam(value = "type") ViewType type,
        @PathVariable Long standardId) throws WitcurveException, URISyntaxException{
        log.debug("Request to get events on given date : {} for standard id : {}", eventDate, standardId);

        //add null checks later and change log statement
        List<EventDTO> result = new ArrayList<>();

//        if(type.equals(ViewType.LEAVE)) {
//            result = eventService.getAllLeavesForStandard(eventDate, standardId);
//        }

        return new ResponseEntity<>(result,  HttpStatus.OK);
    }

    /**
     *
     * @param termId
     * @param studentId
     * @param staffId
     * @param pageable
     * @return
     */
    @GetMapping("/event/term/{termId}")
    @Timed
    public ResponseEntity<Page<EventDTO>> getNoticesForTerm(@ApiParam Pageable pageable,
                                                            @PathVariable Long termId,
                                                            @RequestParam(required = false) Long studentId,
                                                            @RequestParam(required = false) Long staffId) throws WitcurveException, URISyntaxException {
        if(staffId != null) {
            log.debug("Request to get staff notices events on given for term id : {} and staff id : {}", termId, staffId);
        } else if (studentId != null){
            log.debug("Request to get student notices events on given for term id : {} and studentId : {}", termId, studentId);
        } else {
            throw new WitcurveException("Invalid request, there should be one student id or staff id but not both");
        }
        Page<EventDTO> result = eventService.getNotices(termId, studentId, staffId, pageable);

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


}
