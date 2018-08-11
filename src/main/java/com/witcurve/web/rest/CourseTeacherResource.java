package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.CourseTeacher;
import com.witcurve.service.CourseTeacherService;
import com.witcurve.service.dto.CourseTeacherDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class CourseTeacherResource {


    private final Logger log = LoggerFactory.getLogger(CourseTeacher.class);

    @Autowired
    CourseTeacherService courseTeacherService;

    /**
     * creates a courseTeacher
     * @param courseTeacherDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/course-teacher")
    @Timed
    public ResponseEntity<CourseTeacherDTO> createCourseUpdate(@RequestBody @Valid CourseTeacherDTO courseTeacherDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save courseTeacher");
        if (courseTeacherDTO.getId() != null) {
            throw new WitcurveException("New courseTeacher can't already have an id");
        }
        CourseTeacherDTO result = courseTeacherService.saveOrUpdate(courseTeacherDTO);
        return ResponseEntity.created(new URI("/api/course-teacher/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("courseTeacher", result.getId().toString()))
            .body(result);
    }

    /**
     * get courseTeacher by id
     * @param courseTeacherId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/course-teacher/{courseTeacherId}")
    @Timed
    public ResponseEntity<CourseTeacherDTO> getCourseUpdateById(@PathVariable("courseTeacherId") Long courseTeacherId) throws WitcurveException {
        log.debug("Request to get CourseTeacher with id {}", courseTeacherId);
        CourseTeacherDTO result = courseTeacherService.getCourseTeacherById(courseTeacherId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given courseTeacher
     * @param courseTeacherDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/course-teacher")
    @Timed
    public ResponseEntity<CourseTeacherDTO> updateCourseUpdate(@RequestBody @Valid CourseTeacherDTO courseTeacherDTO) throws WitcurveException {
        log.debug("Request to update courseTeacher");
        if (courseTeacherDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        CourseTeacherDTO result = courseTeacherService.saveOrUpdate(courseTeacherDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("courseTeacher", courseTeacherDTO.getId().toString()))
            .body(result);
    }
}
