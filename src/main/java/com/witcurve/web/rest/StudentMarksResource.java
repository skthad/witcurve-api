package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.service.StudentMarksService;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StudentMarksResource {
    private final Logger log = LoggerFactory.getLogger(StandardResource.class);

    @Autowired
    StudentMarksService studentMarksService;
    /**
      * creates a new student marks relation
      * @param studentMarksDTO
      * @return
      * @throws WitcurveException
      * @throws URISyntaxException
      */
    @PostMapping("/student-marks")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> createStudentMarks(@RequestBody @Valid List<StudentMarksDTO> studentMarksDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request create student Marks Relation !!");
        if (studentMarksDTO.get(0).getId()!=null) {
            throw new WitcurveException("New student marks relation can't already have an id");
        }
        List<StudentMarksDTO> result = studentMarksService.saveOrUpdateStudentMarks(studentMarksDTO);

        return ResponseEntity.ok()
            .body(result);
    }

    @PutMapping("/student-marks")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> updateStudentMarks(@RequestBody @Valid List<StudentMarksDTO> studentMarksDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to update student marks");
        if (studentMarksDTO.get(0).getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        List<StudentMarksDTO> result = studentMarksService.saveOrUpdateStudentMarks(studentMarksDTO);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
      * get student marks by id
      * @param studentMarksId
      * @return
      * @throws WitcurveException
     */
    @GetMapping("/student-marks/{studentMarksId}")
    @Timed
    public ResponseEntity<StudentMarksDTO> getStudentMarksById(@PathVariable("studentMarksId") Long studentMarksId) throws WitcurveException {
        log.debug("Request to get Student marks by id");
        StudentMarksDTO result = studentMarksService.getStudentMarksById(studentMarksId);
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
        studentMarksService.deleteStudentMarks(studentMarksId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A student marks relation is deleted with identifier " + studentMarksId,
            studentMarksId.toString())).build();
    }

    /**
      * get student marks in a course teacher by event id for test and assignment only
      * @param eventId
      * @return
      * @throws WitcurveException
     */
    @GetMapping("/student-marks/event/{eventId}")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> getListStudentMarksByCourseTeacher(@PathVariable("eventId")  Long eventId) throws WitcurveException {
        log.debug("Request to get list of Student marks by course teacher id and event Id for test and assignment only .");
        List<StudentMarksDTO> result = studentMarksService.getListStudentMarksInACourseTeacherByEventId(eventId);
        return ResponseEntity.ok(result);
    }

    /**
     * get student marks in course Teacher id by examCourseDetails id for Exam only
     * @param examCourseDetailsId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/student-marks/exam-course-details/{examCourseDetailsId}")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> getListStudentMarksByCourseTeacherAndEcd(@PathVariable("examCourseDetailsId") Long examCourseDetailsId) throws WitcurveException {
        log.debug("Request to get list of Student marks in course teacher id by ecd id for EXAM only .");
        List<StudentMarksDTO> result = studentMarksService.getListStudentMarksForExamInACourseTeacher(examCourseDetailsId);
        return ResponseEntity.ok(result);
    }

    /**
     * get student marks by course Teacher id
     * @param courseTeacherId, studentId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/student-marks/course-teacher/{courseTeacherId}")
    @Timed
    public ResponseEntity<Map<EventType,List<StudentMarksDTO>>> getListMarksForStudentInACourseTeacher(@PathVariable("courseTeacherId")  Long courseTeacherId, @RequestParam Long studentId) throws WitcurveException {
        log.debug("Request to get list of Student marks by course teacher id and event type and student id");
        Map<EventType,List<StudentMarksDTO>> result = studentMarksService.getAllMarksForStudent(courseTeacherId,studentId);
        return ResponseEntity.ok(result);
    }
}


