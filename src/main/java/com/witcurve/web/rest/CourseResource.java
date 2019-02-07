package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.CourseService;
import com.witcurve.service.dto.CourseDTO;
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
public class CourseResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    @Autowired
    CourseService courseService;

    /**
     * creates a course
     * @param courseDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/courses")
    @Timed
    public ResponseEntity<CourseDTO> createCourseUpdate(@RequestBody @Valid CourseDTO courseDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save course");
        if (courseDTO.getId() != null) {
            throw new WitcurveException("New course can't already have an id");
        }
        CourseDTO result = courseService.saveOrUpdate(courseDTO);
        return ResponseEntity.created(new URI("/api/course/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("course", result.getId().toString()))
            .body(result);
    }

    /**
     * update the given course
     * @param courseDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/courses")
    @Timed
    public ResponseEntity<CourseDTO> updateCourseUpdate(@RequestBody @Valid CourseDTO courseDTO) throws WitcurveException {
        log.debug("Request to update course");
        if (courseDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        } else {
            courseService.getCourseById(courseDTO.getId());
        }
        CourseDTO result = courseService.saveOrUpdate(courseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("course", courseDTO.getId().toString()))
            .body(result);
    }

    /**
     * get course by id
     * @param courseId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/courses/{courseId}")
    @Timed
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable("courseId") Long courseId) throws WitcurveException {
        log.debug("Request to get Course with id {}", courseId);
        CourseDTO result = courseService.getCourseById(courseId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get course by grade
     * @param grade
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/courses/school-info/{schoolInfoId}/grades/{grade}")
    @Timed
    public ResponseEntity<List<CourseDTO>> getCourseById(@PathVariable("schoolInfoId") Long schoolInfoId,
                                                         @PathVariable("grade") Grade grade) throws WitcurveException {
        log.debug("Request to get Courses with grade {}", grade);
        List<CourseDTO> result = courseService.getCourseBySchoolInfoAndGrade(schoolInfoId, grade);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the course
     * @param courseId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/courses/{courseId}")
    @Timed
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) throws WitcurveException {
        log.debug("REST request to delete Course: {}", courseId);
        try {
            courseService.deleteCourse(courseId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A course is deleted with identifier " + courseId,
                courseId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }


}
