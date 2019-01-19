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
    public ResponseEntity<CourseTeacherDTO> createCourseTeacher(@RequestBody @Valid CourseTeacherDTO courseTeacherDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save courseTeacher");
        if (courseTeacherDTO.getId() != null) {
            throw new WitcurveException("New courseTeacher can't already have an id");
        }
        try {
            CourseTeacherDTO result = courseTeacherService.saveOrUpdate(courseTeacherDTO);
            return ResponseEntity.created(new URI("/api/course-teacher/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("courseTeacher", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("course_teacher_class_UK")) {
                throw new WitcurveException("Unique constraint (course_id, teacher_id, standard_id) violated");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }

        }
    }

    /**
     * update the given courseTeacher
     * @param courseTeacherDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/course-teacher")
    @Timed
    public ResponseEntity<CourseTeacherDTO> updateCourseTeacher(@RequestBody @Valid CourseTeacherDTO courseTeacherDTO) throws WitcurveException {
        log.debug("Request to update courseTeacher");
        if (courseTeacherDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        try {
            CourseTeacherDTO result = courseTeacherService.saveOrUpdate(courseTeacherDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("courseTeacher", courseTeacherDTO.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("course_teacher_class_UK")) {
                throw new WitcurveException("Unique constraint (course_id, teacher_id, standard_id) violated");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get courseTeacher by id
     * @param courseTeacherId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/course-teacher/{courseTeacherId}")
    @Timed
    public ResponseEntity<CourseTeacherDTO> getCourseTeacherById(@PathVariable("courseTeacherId") Long courseTeacherId) throws WitcurveException {
        log.debug("Request to get CourseTeacher with id {}", courseTeacherId);
        CourseTeacherDTO result = courseTeacherService.getCourseTeacherById(courseTeacherId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the courseTeacher
     * @param courseTeacherId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/course-teacher/{courseTeacherId}")
    @Timed
    public ResponseEntity<Void> deleteCourseTeacher(@PathVariable Long courseTeacherId) throws WitcurveException {
        log.debug("REST request to delete CourseTeacher: {}", courseTeacherId);
        courseTeacherService.deleteCourseTeacher(courseTeacherId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A courseTeacher is deleted with identifier " + courseTeacherId,
            courseTeacherId.toString())).build();
    }

    /**
     * get courseTeacher by teacherId
     * @param teacherId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/course-teacher/teacher/{teacherId}")
    @Timed
    public ResponseEntity<List<CourseTeacherDTO>> getCoursesByTeacherId(
        @PathVariable(value = "teacherId") Long teacherId) {
        log.debug("Request to get CourseTeacher with teacher id {}", teacherId);

        List<CourseTeacherDTO> result = courseTeacherService.getCourseTeachersByTeacherId(teacherId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/course-teacher/standard/{standardId}")
    @Timed
    public ResponseEntity<List<CourseTeacherDTO>> getCoursesByStandardId(@PathVariable("standardId") Long standardId) throws WitcurveException {
        log.debug("Request to get Course with standardId {}", standardId);
        List<CourseTeacherDTO> result = courseTeacherService.getCoursesByStandardId(standardId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/course-teacher/student/{studentId}")
    @Timed
    public ResponseEntity<List<CourseTeacherDTO>> getCourseTeacherByStudentId(@PathVariable("studentId") Long studentId) throws WitcurveException {
        log.debug("Request to get Course teacher with studentId {}", studentId);
        List<CourseTeacherDTO> result = courseTeacherService.getCourseTeachersByStudentId(studentId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
