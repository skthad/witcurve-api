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
    @PostMapping("/exam-course-details")
    @Timed
    public ResponseEntity<ExamCourseDetailsDTO> createExamCourseDetails(@RequestBody @Valid ExamCourseDetailsDTO examCourseDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save examCourseDetails");
        if (examCourseDetailsDTO.getId() != null) {
            throw new WitcurveException("New examCourseDetailsDTO can't already have an id");
        }
        ExamCourseDetailsDTO result = examCourseDetailsService.saveOrUpdate(examCourseDetailsDTO);
        return ResponseEntity.created(new URI("/api/slot-course-details/"))
            .headers(HeaderUtil.createEntityUpdateAlert("examCourseDetails", ""))
            .body(result);
    }/**
     * updates an examCourseDetails
     *
     * @param examCourseDetailsDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/exam-course-details")
    @Timed
    public ResponseEntity<ExamCourseDetailsDTO> updateExamCourseDetails(@RequestBody @Valid ExamCourseDetailsDTO examCourseDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save examCourseDetails");
        if (examCourseDetailsDTO.getId() == null) {
            throw new WitcurveException("An update request for examCourseDetailsDTO must have an id");
        }
        ExamCourseDetailsDTO result = examCourseDetailsService.saveOrUpdate(examCourseDetailsDTO);
        return ResponseEntity.created(new URI("/api/slot-course-details/"))
            .headers(HeaderUtil.createEntityUpdateAlert("examCourseDetails", ""))
            .body(result);
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

    /**
     * get examCourseDetails by standard id
     *
     * @param grade
     * @param examId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/exam-course-details/grade/{grade}/exam/{examId}")
    @Timed
    public ResponseEntity<List<ExamCourseDetailsDTO>> getExamCourseDetailsByGradeAndExam(@PathVariable("grade") Grade grade,
                                                                                         @PathVariable("examId") Long examId) throws WitcurveException {
        log.debug("Request to get ExamCourseDetails with grade: {} and examId: {}", grade, examId);
        List<ExamCourseDetailsDTO> result = examCourseDetailsService.getExamCourseDetailsByGradeAndExamId(grade, examId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get examCourseDetails by teacher id and exam id
     *
     * @param teacherId
     * @oaram examId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/exam-course-details/teacher/{teacherId}")
    @Timed
    public ResponseEntity<List<ExamCourseDetailsDTO>> getExamCourseDetailsByTeacherAndExamId(@PathVariable("teacherId") Long teacherId,
                                                                                             @RequestParam("examId") Long examId) throws WitcurveException {
        log.debug("Request to get ExamCourseDetails with teacherId: {} and examId: {}", teacherId, examId);
        List<ExamCourseDetailsDTO> result = examCourseDetailsService.getExamCourseDetailsByTeacherIdAndExamId(teacherId, examId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
