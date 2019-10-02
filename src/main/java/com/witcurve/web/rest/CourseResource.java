package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.CourseService;
import com.witcurve.service.dto.CourseDTO;
import com.witcurve.service.dto.CourseTrackDTO;
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
     *
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
        try {
            CourseDTO result = courseService.saveOrUpdate(courseDTO);
            return ResponseEntity.created(new URI("/api/course/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("course", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("course_code_grade_school_info_id_UK")) {
                throw new WitcurveException("There already exists a subject code with given subject code details for this grade");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * update the given course
     *
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
        try {
            CourseDTO result = courseService.saveOrUpdate(courseDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("course", courseDTO.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("course_code_grade_school_info_id_UK")) {
                throw new WitcurveException("There already exists a subject code with given subject code details for this grade");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get course by id
     *
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
     * get course by grade and schoolInfoId
     *
     * @param grade
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/courses/school-info/{schoolInfoId}/grades/{grade}")
    @Timed
    public ResponseEntity<List<CourseDTO>> getCourseById(@PathVariable("schoolInfoId") Long schoolInfoId,
                                                         @PathVariable("grade") Grade grade,
                                                         @RequestParam(value = "courseType", required = false) CourseType courseType,
                                                         @RequestParam(value = "elective", required = false, defaultValue = "false") Boolean elective,
                                                         @RequestParam(value = "mandatory", required = false, defaultValue = "false") Boolean mandatory) throws WitcurveException {
        log.debug("Request to get Courses with grade {}", grade);
        List<CourseDTO> result = courseService.getCourseBySchoolInfoAndGrade(schoolInfoId, grade, courseType, elective, mandatory);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get course by grade and examId
     *
     * @param grade
     * @param examId
     * @param courseType
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/courses/exam/{examId}/grades/{grade}")
    @Timed
    public ResponseEntity<List<CourseDTO>> getCourseByExamAndGrde(@PathVariable("examId") Long examId,
                                                         @PathVariable("grade") Grade grade, @RequestParam CourseType courseType) throws WitcurveException {
        log.debug("Request to get Courses with grade {} for exam with id : {} of course type : {}", grade, examId, courseType);
        List<CourseDTO> result = courseService.getCourseByExamIdAndGradeAndCourseType(examId, grade, courseType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * delete the course
     *
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

    /**
     * get course tracking by id
     *
     * @param courseId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/courses/{courseId}/track")
    @Timed
    public ResponseEntity<CourseTrackDTO> getCourseTrackById(@PathVariable Long courseId,
                                                             @RequestParam(required = false) Long staffId) throws WitcurveException {
        log.debug("Request to course tracking with id {} and staff id {}", courseId, staffId);
        CourseTrackDTO result = courseService.getCourseTrackById(courseId, staffId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get course by studentId
     *
     * @param studentId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/courses/student/{studentId}")
    @Timed
    public ResponseEntity<List<CourseDTO>> getStudentCourseByStudentId(@PathVariable Long studentId) throws WitcurveException {
        log.debug("Request to get Course with student with id {}", studentId);
        List<CourseDTO> result = courseService.getCourseByStudentId(studentId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

