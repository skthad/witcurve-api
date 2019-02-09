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
import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseTeacherResource {


    private final Logger log = LoggerFactory.getLogger(CourseTeacher.class);

    @Autowired
    CourseTeacherService courseTeacherService;

    /**
     * creates a courseTeachers
     * @param courseTeacherDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/course-teacher")
    @Timed
    public ResponseEntity<List<CourseTeacherDTO>> createCourseTeacher(@RequestBody @Valid List<CourseTeacherDTO> courseTeacherDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request Save courseTeacher");
        Long standardId = courseTeacherDTOs.get(0).getStandard().getId();
        for(CourseTeacherDTO courseTeacherDTO : courseTeacherDTOs) {
            if (courseTeacherDTO.getId() != null) {
                throw new WitcurveException("New courseTeacher can't already have an id");
            }
            if(standardId != courseTeacherDTO.getStandard().getId()) {
                throw new WitcurveException("CourseTeachers belonging to same standard id can be saved at a time");
            }
        }
        try {
            List<CourseTeacherDTO> result = courseTeacherService.saveOrUpdate(courseTeacherDTOs);
            return ResponseEntity.created(new URI("/api/course-teacher/"))
                .headers(HeaderUtil.createEntityCreationAlert("courseTeacher", "created"))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("course_teacher_standard_UK")) {
                throw new WitcurveException("Unique constraint (course_id, teacher_id, standard_id) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * update the given courseTeachers
     * @param courseTeacherDTOs
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/course-teacher")
    @Timed
    public ResponseEntity<List<CourseTeacherDTO>> updateCourseTeacher(@RequestBody @Valid List<CourseTeacherDTO> courseTeacherDTOs) throws WitcurveException {
        log.debug("Request to update courseTeacher");
        Long standardId = courseTeacherDTOs.get(0).getStandard().getId();
        for(CourseTeacherDTO courseTeacherDTO : courseTeacherDTOs) {
            if (courseTeacherDTO.getId() == null) {
                throw new WitcurveException("CourseTeachers should have id to update");
            }
            if(standardId != courseTeacherDTO.getStandard().getId()) {
                throw new WitcurveException("CourseTeachers belonging to same standard id can be saved at a time");
            }
        }
        try {
            List<CourseTeacherDTO> result = courseTeacherService.saveOrUpdate(courseTeacherDTOs);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("courseTeacher", "updated"))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("course_teacher_standard_UK")) {
                throw new WitcurveException("Unique constraint (course_id, teacher_id, standard_id) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get courseTeacher suggestions
     * @param gsdId
     * @param dayOfWeek
     *
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/course-teacher/suggestion")
    @Timed
    public ResponseEntity<List<CourseTeacherDTO>> suggestCourseTeachers(@RequestParam("gsdId") Long gsdId,
                                                                  @RequestParam("dayOfWeek") DayOfWeek dayOfWeek) throws WitcurveException {
        log.debug("Request to suggest CourseTeachers for gsd id ", gsdId);
        List<CourseTeacherDTO> result = courseTeacherService.getCourseTeacherSuggestionForSlot(gsdId, dayOfWeek);
        return new ResponseEntity<>(result, HttpStatus.OK);
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
     * get courseTeacher by teacherId
     * @param teacherId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/course-teacher/teachers/{teacherId}")
    @Timed
    public ResponseEntity<List<CourseTeacherDTO>> getCoursesByTeacherId(
        @PathVariable(value = "teacherId") Long teacherId) {
        log.debug("Request to get CourseTeacher with teacher id {}", teacherId);

        List<CourseTeacherDTO> result = courseTeacherService.getCourseTeachersByTeacherId(teacherId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/course-teacher/standards/{standardId}")
    @Timed
    public ResponseEntity<List<CourseTeacherDTO>> getCoursesByStandardId(@PathVariable("standardId") Long standardId) throws WitcurveException {
        log.debug("Request to get Course with standardId {}", standardId);
        List<CourseTeacherDTO> result = courseTeacherService.getCoursesByStandardId(standardId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/course-teacher/students/{studentId}")
    @Timed
    public ResponseEntity<List<CourseTeacherDTO>> getCourseTeacherByStudentId(@PathVariable("studentId") Long studentId) throws WitcurveException {
        log.debug("Request to get Course teacher with studentId {}", studentId);
        List<CourseTeacherDTO> result = courseTeacherService.getCourseTeachersByStudentId(studentId);
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
        try {
            courseTeacherService.deleteCourseTeacher(courseTeacherId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A courseTeacher is deleted with identifier " + courseTeacherId,
                courseTeacherId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }
}
