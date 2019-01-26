package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.Grade;
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
     * @param examCourseDetailsDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/exam-course-details/exams/{examId}")
    @Timed
    public ResponseEntity<ExamCourseDetailsDTO> createExamCourseDetails(@RequestBody @Valid ExamCourseDetailsDTO examCourseDetailsDTO,
                                                                        @PathVariable Long examId) throws WitcurveException, URISyntaxException {
        log.debug("Request Save examCourseDetails");
        if (examCourseDetailsDTO.getId() != null) {
            throw new WitcurveException("New examCourseDetailsDTO can't already have an id");
        }
        try {
            ExamCourseDetailsDTO result = examCourseDetailsService.saveOrUpdate(examCourseDetailsDTO, examId);
            return ResponseEntity.created(new URI("/api/slot-course-details/"))
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
     * updates an examCourseDetails
     *
     * @param examCourseDetailsDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/exam-course-details/exams/{examId}")
    @Timed
    public ResponseEntity<ExamCourseDetailsDTO> updateExamCourseDetails(@RequestBody @Valid ExamCourseDetailsDTO examCourseDetailsDTO,
                                                                        @PathVariable Long examId) throws WitcurveException, URISyntaxException {
        log.debug("Request Save examCourseDetails");
        if (examCourseDetailsDTO.getId() == null) {
            throw new WitcurveException("An update request for examCourseDetailsDTO must have an id");
        }
        try {
            ExamCourseDetailsDTO result = examCourseDetailsService.saveOrUpdate(examCourseDetailsDTO, examId);
            return ResponseEntity.created(new URI("/api/slot-course-details/"))
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

    @GetMapping("/exam-course-details/grade/{grade}/exams/{examId}")
    @Timed
    public ResponseEntity<List<ExamCourseDetailsDTO>> getExamCourseDetailsByGradeAndExam(@PathVariable("grade") Grade grade,
                                                                                         @PathVariable("examId") Long examId) throws WitcurveException {
        log.debug("Request to get ExamCourseDetails with grade: {} and examId: {}", grade, examId);
        List<ExamCourseDetailsDTO> result = examCourseDetailsService.getExamCourseDetailsByGradeAndExamId(grade, examId);
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
