package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ViewType;
import com.witcurve.service.ExamCourseDetailsService;
import com.witcurve.service.dto.ExamCourseDetailsDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExamCourseDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ExamCourseDetailsResource.class);

    @Autowired
    ExamCourseDetailsService examCourseDetailsService;

    /**
     * creates an examCourseDetails
     *
     * @param examCourseDetailsDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/exam-course-details/exams/{examId}")
    @Timed
    public ResponseEntity<List<ExamCourseDetailsDTO>> createExamCourseDetails(@RequestBody @Valid List<ExamCourseDetailsDTO> examCourseDetailsDTOs,
                                                                        @PathVariable Long examId) throws WitcurveException, URISyntaxException {
        log.debug("Request Save examCourseDetails");
        try {
            List<ExamCourseDetailsDTO> result = examCourseDetailsService.saveOrUpdate(examCourseDetailsDTOs, examId);
            return ResponseEntity.created(new URI("/api/exam-course-details/exams/" + examId))
                .headers(HeaderUtil.createEntityUpdateAlert("examCourseDetails", ""))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("ecd_slot_date_course_UK")) {
                throw new WitcurveException("Unique constraint (gsd_id, date, course) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get examCourseDetails by id
     *
     * @param examCourseDetailsId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/exam-course-details/{examCourseDetailsId}")
    @Timed
    public ResponseEntity<ExamCourseDetailsDTO> getExamCourseDetailsById(@PathVariable("examCourseDetailsId") Long examCourseDetailsId) throws WitcurveException {
        log.debug("Request to get ExamCourseDetails with id {}", examCourseDetailsId);
        ExamCourseDetailsDTO result = examCourseDetailsService.getExamCourseDetailsById(examCourseDetailsId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get examCourseDetails by standard id
     *
     * @param grade
     * @param examId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/exam-course-details/grades/{grade}/exams/{examId}")
    @Timed
    public ResponseEntity<List<ExamCourseDetailsDTO>> getExamCourseDetailsByGradeAndExam(@PathVariable("grade") Grade grade,
                                                                                         @PathVariable("examId") Long examId) throws WitcurveException {
        log.debug("Request to get ExamCourseDetails with grade: {} and examId: {}", grade, examId);
        List<ExamCourseDetailsDTO> result = examCourseDetailsService.getExamCourseDetailsByGradeAndExamId(grade, examId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/exam-course-details/students/{studentId}")
    @Timed
    public ResponseEntity<List<ExamCourseDetailsDTO>> getExamCourseDetailsForStudentOnDate(@PathVariable("studentId") Long studentId,
                                                                                           @RequestParam(value = "eventDate", required = false) LocalDate eventDate,
                                                                                           @RequestParam(value = "month", required = false) Integer month,
                                                                                           @RequestParam(value = "year", required = false) Integer year,
                                                                                           @RequestParam(value = "type") ViewType type) throws WitcurveException{
        log.debug("Request to get ExamCourseDetails for student with id : {} and for view type : {}", studentId, type);
        List<ExamCourseDetailsDTO> result = new ArrayList<>();
        if(ViewType.DAY.equals(type)) {
            if(eventDate == null) {
                throw new WitcurveException("There should be eventDate param for DAY view");
            }
            result = examCourseDetailsService.getExamCourseDetailsForStudentOnDate(studentId, eventDate);
        } else if(ViewType.MONTH.equals(type)){
            if(month == null || year ==null) {
                throw new WitcurveException("There should be month and year param for MONTH view");
            }
            result = examCourseDetailsService.getExamCourseDetailsOnAGivenMonthForStudent(studentId, month, year);
        } else if(ViewType.UPCOMING_EVENTS.equals(type)) {
            if (eventDate == null) {
                throw new WitcurveException("There should be eventDate param for Upcoming Events view");
            }
            result = examCourseDetailsService.getUpcomingExamCourseDetailsForStudent(studentId, eventDate);
        }  else if(ViewType.DIARY.equals(type)) {
            if (eventDate == null) {
                throw new WitcurveException("There should be eventDate param for Upcoming Events view");
            }
            result = examCourseDetailsService.getDairyCourseDetailsForStudent(studentId, eventDate);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/exam-course-details/staff/{staffId}")
    @Timed
    public ResponseEntity<List<ExamCourseDetailsDTO>> getExamCourseDetailsForStaffOnDate(@PathVariable("staffId") Long staffId,
                                                                                           @RequestParam(value = "eventDate", required = false) LocalDate eventDate,
                                                                                           @RequestParam(value = "month", required = false) Integer month,
                                                                                           @RequestParam(value = "year", required = false) Integer year,
                                                                                           @RequestParam(value = "type") ViewType type) throws WitcurveException{
        log.debug("Request to get ExamCourseDetails for staff with id : {} and for view type : {}", staffId, type);
        List<ExamCourseDetailsDTO> result = new ArrayList<>();
        if(ViewType.DAY.equals(type)) {
            if(eventDate == null) {
                throw new WitcurveException("There should be eventDate param for DAY view");
            }
            result = examCourseDetailsService.getExamCourseDetailsForStaffOnDate(staffId, eventDate);
        } else if(ViewType.MONTH.equals(type)){
            if(month == null || year ==null) {
                throw new WitcurveException("There should be month and year param for MONTH view");
            }
            result = examCourseDetailsService.getExamCourseDetailsOnAGivenMonthForStaff(staffId, month, year);
        } else if(ViewType.UPCOMING_EVENTS.equals(type)) {
            if (eventDate == null) {
                throw new WitcurveException("There should be eventDate param for Upcoming Events view");
            }
            result = examCourseDetailsService.getUpcomingExamCourseDetailsForStaff(staffId, eventDate);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the examCourseDetails
     * @param examCourseDetailsId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/exam-course-details/{examCourseDetailsId}")
    @Timed
    public ResponseEntity<Void> deleteExamCourseDetails(@PathVariable Long examCourseDetailsId) throws WitcurveException {
        log.debug("REST request to delete ExamCourseDetails: {}", examCourseDetailsId);
        try {
            examCourseDetailsService.deleteExamCourseDetails(examCourseDetailsId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A examCourseDetails is deleted with identifier " + examCourseDetailsId,
                examCourseDetailsId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }


}
