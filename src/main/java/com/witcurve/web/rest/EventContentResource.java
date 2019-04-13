package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.EventContentService;
import com.witcurve.service.dto.EventContentDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EventContentResource {

    private final Logger log = LoggerFactory.getLogger(EventContentResource.class);

    @Autowired
    EventContentService eventContentService;

    /**
     * creates eventContents for event
     * @param eventId
     *
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/event-content/events/{eventId}")
    @Timed
    public ResponseEntity<List<EventContentDTO>> createEventContentsForEvent(@RequestBody @Valid List<EventContentDTO> eventContentDTOs,
                                                                     @PathVariable("eventId") Long eventId) throws WitcurveException, URISyntaxException {
        log.debug("Request Save or Update eventContents for event with ID: {}", eventId);

        try {
            List<EventContentDTO> result = eventContentService.saveOrUpdateForEvent(eventId, eventContentDTOs, false);
            return ResponseEntity.created(new URI("/api/event-content/events/" + eventId))
                .headers(HeaderUtil.createEntityCreationAlert("eventContent", null))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("event_content_id_UK")) {
                throw new WitcurveException("Unique constraint (event_id, course_content_id, for_exam) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

    /**
     * creates eventContents for exam
     * @param examId
     *
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/event-content/exams/{examId}")
    @Timed
    public ResponseEntity<List<EventContentDTO>> createEventContentsForExam(@RequestBody @Valid List<EventContentDTO> eventContentDTOs,
                                                                     @PathVariable("examId") Long examId) throws WitcurveException, URISyntaxException {
        log.debug("Request Save or Update eventContents for exam with ID: {}", examId);

        try {
            List<EventContentDTO> result = eventContentService.saveOrUpdateForEvent(examId, eventContentDTOs, true);
            return ResponseEntity.created(new URI("/api/event-content/exams/" + examId))
                .headers(HeaderUtil.createEntityCreationAlert("eventContent", null))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("event_content_id_UK")) {
                throw new WitcurveException("Unique constraint (event_id, course_content_id, for_exam) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

    /**
     * delete the eventContent
     * @param eventContentId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/event-content/{eventContentId}")
    @Timed
    public ResponseEntity<Void> deleteEventContent(@PathVariable Long eventContentId) throws WitcurveException {
        log.debug("REST request to delete EventContent: {}", eventContentId);

        try {
            eventContentService.deleteEventContentById(eventContentId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("An eventContent is deleted with identifier " + eventContentId,
                eventContentId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * delete the eventContents by event
     * @param eventId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/event-content/events/{eventId}")
    @Timed
    public ResponseEntity<Void> deleteEventContentsByEvent(@PathVariable Long eventId) throws WitcurveException {
        log.debug("REST request to delete EventContents with eventId: {}", eventId);

        try {
            eventContentService.deleteEventContentByEventId(eventId, false);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("All courseContents are unlinked with eventId " + eventId,
                eventId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * delete the eventContents by exam
     * @param examId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/event-content/exams/{examId}")
    @Timed
    public ResponseEntity<Void> deleteEventContentsByExam(@PathVariable Long examId) throws WitcurveException {
        log.debug("REST request to delete EventContents with examId: {}", examId);

        try {
            eventContentService.deleteEventContentByEventId(examId, true);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("All courseContents are unlinked with examId " + examId,
                examId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }
}
