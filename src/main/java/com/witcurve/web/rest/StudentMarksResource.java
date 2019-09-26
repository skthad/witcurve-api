package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.dto.StudentMarksDTO;
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
public class StudentMarksResource {

    private final Logger log = LoggerFactory.getLogger(StandardResource.class);

    @Autowired
    StudentMarksService studentMarksService;
    /**
      * creates a new student marks
      * @param studentMarksDTOs
      * @param eventId
      * @param rcdId
      * @return
      * @throws WitcurveException
      * @throws URISyntaxException
      */
    @PostMapping("/student-marks")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> createStudentMarks(@RequestBody @Valid List<StudentMarksDTO> studentMarksDTOs,
                                                                    @RequestParam(required = false) Long eventId,
                                                                    @RequestParam(required = false) Long rcdId,
                                                                    @RequestParam(required = false) Long courseId) throws WitcurveException, URISyntaxException {
        log.debug("Request to create student Marks ");
        List<StudentMarksDTO> result = studentMarksService.saveOrUpdateStudentMarks(studentMarksDTOs, eventId, rcdId, courseId);
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
     * @param rcdId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/student-marks/exams/{examId}")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> getStudentMarksForExam(@PathVariable("examId")  Long examId,
                                                                        @RequestParam(value = "courseId", required = false) Long courseId,
                                                                        @RequestParam(value = "rcdId", required = false) Long rcdId,
                                                                        @RequestParam(value = "standardId", required = false) Long standardId) throws WitcurveException {
        log.debug("Request to get list of Student marks by exam");
        List<StudentMarksDTO> result = studentMarksService.getStudentMarksByExamId(examId, courseId, rcdId, standardId);
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
                                                                                 @RequestParam(value = "endDate") LocalDate endDate) throws WitcurveException {
        log.debug("Request to get list of Student marks by course id and event type and student id");
        List<StudentMarksDTO> result = studentMarksService.getAllMarksForAStudentInACourse(studentId, courseId, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    /**
     * delete the studentMarks with ids
     * @param ids
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/student-marks")
    @Timed
    public ResponseEntity<Void> deleteStudentMarks(@RequestParam List<Long> ids) throws WitcurveException {
        log.debug("REST request to delete student marks: with ids {}", ids);
        studentMarksService.deleteStudentMarks(ids);
        return ResponseEntity.ok(null);
    }
}


