package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentMarksResource {
    private final Logger log = LoggerFactory.getLogger(StandardResource.class);

    @Autowired
    StudentMarksService studentMarksService;
    /**
      * creates a new student marks relation
      * @param studentMarksDTOs
      * @param eventId
      * @param ecdId
      * @param rcdId
      * @return
      * @throws WitcurveException
      * @throws URISyntaxException
      */
    @PostMapping("/student-marks")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> createStudentMarks(@RequestBody @Valid List<StudentMarksDTO> studentMarksDTOs,
                                                                    @RequestParam(required = false) Long ecdId,
                                                                    @RequestParam(required = false) Long eventId,
                                                                    @RequestParam(required = false) Long rcdId) throws WitcurveException, URISyntaxException {
        log.debug("Request to create student Marks ");
        List<StudentMarksDTO> result = studentMarksService.saveOrUpdateStudentMarks(studentMarksDTOs, ecdId, eventId, rcdId);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
      * get student marks by event id
      * @param eventId
      * @param rcdId
      * @return
      * @throws WitcurveException
     */
    @GetMapping("/student-marks/events/{eventId}")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> getStudentMarksForTestOrAssignmentEvent(@PathVariable("eventId")  Long eventId, @RequestParam(required = false) Long rcdId) throws WitcurveException {
        log.debug("Request to get list of student marks by eventId: {} for rcdId : {}", eventId, rcdId);
        List<StudentMarksDTO> result = studentMarksService.getStudentMarksByEventId(eventId, rcdId);
        return ResponseEntity.ok(result);
    }

    /**
     * get student marks by exam id
     * @param examId
     * @param ecdId
     * @param rcdId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/student-marks/exams/{examId}")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> getStudentMarksForExam(@PathVariable("examId")  Long examId,
                                                                        @RequestParam(value = "ecdId", required = false) Long ecdId,
                                                                        @RequestParam(value = "rcdId", required = false) Long rcdId,
                                                                        @RequestParam(value = "standardId", required = false) Long standardId,
                                                                        @RequestParam(defaultValue = "false") Boolean publishedOnly) throws WitcurveException {
        log.debug("Request to get list of Student marks by exam");
        List<StudentMarksDTO> result = studentMarksService.getStudentMarksByExamId(examId, ecdId, rcdId, standardId, publishedOnly);
        return ResponseEntity.ok(result);
    }

    /**
     * get student marks by course id for a student
     * @param courseId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/student-marks/students/{studentId}/courses/{courseId}")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> getAllMarksForAStudentInACourse(@PathVariable("studentId")  Long studentId,
                                                                                @PathVariable("courseId")  Long courseId,
                                                                                @RequestParam("type") EventType type,
                                                                                 @RequestParam(value = "startDate") LocalDate startDate,
                                                                                 @RequestParam(value = "endDate") LocalDate endDate,
                                                                                 @RequestParam(defaultValue = "false") Boolean publishedOnly) throws WitcurveException {
        log.debug("Request to get list of Student marks by course id and event type and student id");
        List<StudentMarksDTO> result = studentMarksService.getAllMarksForAStudentInACourse(studentId, courseId, type, startDate, endDate, publishedOnly);
        return ResponseEntity.ok(result);
    }

    /**
     * get all student marks by student id for an exam
     * @param studentId
     * @param examId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/student-marks/students/{studentId}/exams/{examId}")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> getAllMarksForAStudentInAExam(@PathVariable("studentId")  Long studentId,
                                                                                 @PathVariable("examId")  Long examId,
                                                                                 @RequestParam(defaultValue = "true") Boolean publishedOnly) throws WitcurveException {
        log.debug("Request to get list of Student marks by student id : {} and  for exam with id : {}");
        List<StudentMarksDTO> result = studentMarksService.getAllMarksForAStudentInAnExam(studentId, examId, publishedOnly);
        return ResponseEntity.ok(result);
    }

    /**
     * get student marks by course id for a grade
     * @param courseId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/student-marks/grades/{grade}/courses/{courseId}")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> getMarksForAllStudentsInAGradeAndCourse(@PathVariable("grade") Grade grade,
                                                                               @PathVariable("courseId") Long courseId,
                                                                              @RequestParam("type") EventType type,
                                                                              @RequestParam(value = "startDate") LocalDate startDate,
                                                                              @RequestParam(value = "endDate") LocalDate endDate,
                                                                              @RequestParam(defaultValue = "false") Boolean publishedOnly) throws WitcurveException {
        log.debug("Request to get list of Student marks by course id and event type and grade");
        List<StudentMarksDTO> result = studentMarksService.getMarksForAllStudentsInAGradeAndCourse(grade, courseId, type, startDate, endDate, publishedOnly);
        return ResponseEntity.ok(result);
    }

    /**
     * get student marks by course id for a grade
     * @param courseId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/student-marks/standards/{standardId}/courses/{courseId}")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> getMarksForAllStudentsInAStandardAndCourse(@PathVariable("standardId") Long standardId,
                                                                                 @PathVariable("courseId") Long courseId,
                                                                                 @RequestParam("type") EventType type,
                                                                                 @RequestParam(value = "startDate") LocalDate startDate,
                                                                                 @RequestParam(value = "endDate") LocalDate endDate) throws WitcurveException {
        log.debug("Request to get list of Student marks by course id and event type and grade");
        List<StudentMarksDTO> result = studentMarksService.getMarksForAllStudentsInAStandardAndCourse(standardId, courseId, type, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    /**
     * delete the studentMarks
     * @param studentMarksId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/student-marks/{studentMarksId}")
    @Timed
    public ResponseEntity<Void> deleteStudentMarks(@PathVariable Long studentMarksId) throws WitcurveException {
        log.debug("REST request to delete student marks: {}", studentMarksId);
        try {
            studentMarksService.deleteStudentMarks(studentMarksId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A student marks relation is deleted with identifier " + studentMarksId,
                studentMarksId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }
}


