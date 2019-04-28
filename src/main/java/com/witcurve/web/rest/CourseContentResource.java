package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.CourseContentService;
import com.witcurve.service.EventContentService;
import com.witcurve.service.dto.CourseContentDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseContentResource {

    private final Logger log = LoggerFactory.getLogger(CourseContentResource.class);

    @Autowired
    CourseContentService courseContentService;

    @Autowired
    EventContentService eventContentService;

    /**
     * creates courseContents
     * @param courseContentDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/course-content/courses/{courseId}")
    @Timed
    public ResponseEntity<List<CourseContentDTO>> createCourseContents(@RequestBody @Valid List<CourseContentDTO> courseContentDTOs,
                                                                        @PathVariable("courseId") Long courseId) throws WitcurveException, URISyntaxException {
        log.debug("Request to Save or Update courseContents");

        try {
            List<CourseContentDTO> result = courseContentService.saveOrUpdateForCourse(courseId, courseContentDTOs);
            return ResponseEntity.created(new URI("/api/course-content/courses/" + courseId))
                .headers(HeaderUtil.createEntityCreationAlert("courseContent", null))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("course_parent_content_order_UK")) {
                throw new WitcurveException("Unique constraint (course_id, parent_content_id, order) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

    /**
     * update single courseContent
     * @param courseContentDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/course-content/courses/{courseId}")
    @Timed
    public ResponseEntity<CourseContentDTO> updateSingleCourseContent(@RequestBody @Valid CourseContentDTO courseContentDTO,
                                                                       @PathVariable("courseId") Long courseId) throws WitcurveException, URISyntaxException {
        log.debug("Request to update single courseContent");

        try {
            if (courseContentDTO.getId() == null) {
                throw new WitcurveException("An upate request for course content must have an ID");
            }
            CourseContentDTO result = courseContentService.updateSingleCourseContent(courseId, courseContentDTO);
            return ResponseEntity.created(new URI("/api/course-content/courses/" + courseId))
                .headers(HeaderUtil.createEntityUpdateAlert("courseContent", ""))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("course_parent_content_order_UK")) {
                throw new WitcurveException("Unique constraint (course_id, parent_content_id, order) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

    /**
     * get courseContent by id
     * @param courseContentId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/course-content/{courseContentId}")
    @Timed
    public ResponseEntity<CourseContentDTO> getCourseContentsById(@PathVariable(value = "courseContentId") Long courseContentId) throws WitcurveException {

        log.debug("Request to get CourseContent with id {}", courseContentId);

        CourseContentDTO result = courseContentService.getCourseContentById(courseContentId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get courseContents by courseId
     * @param courseId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/course-content/courses/{courseId}")
    @Timed
    public ResponseEntity<List<CourseContentDTO>> getCourseContentsByCourse(@PathVariable("courseId") Long courseId) throws WitcurveException {
        log.debug("Request to get CourseContents with courseId {}", courseId);
        List<CourseContentDTO> result = courseContentService.getCourseContentsByCourseId(courseId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get courseContents by eventId
     * @param eventId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/course-content/events/{eventId}")
    @Timed
    public ResponseEntity<List<CourseContentDTO>> getCourseContentsByEvent(@PathVariable("eventId") Long eventId) throws WitcurveException {
        log.debug("Request to get CourseContents with eventId {}", eventId);
        List<CourseContentDTO> result = courseContentService.getCourseContentsByEventId(eventId, false);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get courseContents by examId
     * @param examId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/course-content/exams/{examId}")
    @Timed
    public ResponseEntity<List<CourseContentDTO>> getCourseContentsByExam(@PathVariable("examId") Long examId) throws WitcurveException {
        log.debug("Request to get CourseContents with examId {}", examId);
        List<CourseContentDTO> result = courseContentService.getCourseContentsByEventId(examId, true);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the courseContent
     * @param courseContentId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/course-content/{courseContentId}")
    @Timed
    public ResponseEntity<Void> deleteCourseContent(@PathVariable Long courseContentId) throws WitcurveException {
        log.debug("REST request to delete CourseContent: {}", courseContentId);

        try {
            courseContentService.deleteCourseContent(courseContentId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A courseContent is deleted with identifier " + courseContentId,
                courseContentId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * delete the courseContents by course
     * @param courseId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/course-content/courses/{courseId}")
    @Timed
    public ResponseEntity<Void> deleteCourseContentsByCourse(@PathVariable Long courseId) throws WitcurveException {
        log.debug("REST request to delete CourseContent with courseId: {}", courseId);

        try {
            courseContentService.deleteCourseContentsByCourseId(courseId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("All courseContent is deleted with courseId " + courseId,
                courseId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }
}
