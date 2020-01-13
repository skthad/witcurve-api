package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.CourseGradeService;
import com.witcurve.service.dto.CourseGradeDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseGradeResource {

    private final Logger log = LoggerFactory.getLogger(CourseGradeResource.class);

    @Autowired
    CourseGradeService courseGradeService;

    /**
     * creates a new student course grade
     *
     * @param courseGradeDTOs
     * @param courseId
     * @param rcdId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/course-grade")
    @Timed
    public ResponseEntity<List<CourseGradeDTO>> createCourseGrade(@RequestBody @Valid List<CourseGradeDTO> courseGradeDTOs,
                                                                   @RequestParam Long rcdId,
                                                                   @RequestParam Long courseId) throws WitcurveException, URISyntaxException {
        log.debug("Request to create student course grade ");
        List<CourseGradeDTO> result = courseGradeService.saveOrUpdateCourseGrade(courseGradeDTOs, rcdId, courseId);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * get course grade by exam id
     *
     * @param examId
     * @param rcdId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/course-grade/exams/{examId}")
    @Timed
    public ResponseEntity<List<CourseGradeDTO>> getCourseGradeForExam(@PathVariable("examId") Long examId,
                                                                      @RequestParam(value = "courseId", required = false) Long courseId,
                                                                      @RequestParam(value = "rcdId", required = false) Long rcdId,
                                                                      @RequestParam(value = "standardId", required = false) Long standardId) throws WitcurveException {
        log.debug("Request to get list of Student grade by exam");
        List<CourseGradeDTO> result = courseGradeService.getCourseGradeByExamId(examId, courseId, rcdId, standardId);
        return ResponseEntity.ok(result);
    }

    /**
     * get student grade by course id for a student
     *
     * @param courseId
     * @param studentId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/course-grade/students/{studentId}/courses/{courseId}")
    @Timed
    public ResponseEntity<List<CourseGradeDTO>> getAllGradesForAStudentInACourse(@PathVariable("studentId") Long studentId,
                                                                                 @PathVariable("courseId") Long courseId,
                                                                                 @RequestParam(value = "startDate") LocalDate startDate,
                                                                                 @RequestParam(value = "endDate") LocalDate endDate) throws WitcurveException {
        log.debug("Request to get list of student grade by course id and student id");
        List<CourseGradeDTO> result = courseGradeService.getAllGradesForAStudentInACourse(studentId, courseId, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    /**
     * delete the studentGrade with ids
     *
     * @param ids
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/course-grade")
    @Timed
    public ResponseEntity<Void> deleteStudentGrades(@RequestParam List<Long> ids) throws WitcurveException {
        log.debug("REST request to delete student grade with ids {}", ids);
        courseGradeService.deleteStudentGrades(ids);
        return ResponseEntity.ok(null);
    }
}
