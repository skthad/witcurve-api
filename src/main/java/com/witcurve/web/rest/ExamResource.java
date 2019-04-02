package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.ExamService;
import com.witcurve.service.dto.ExamDTO;
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
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExamResource {

    private final Logger log = LoggerFactory.getLogger(ExamResource.class);

    @Autowired
    ExamService examService;

    /**
     * creates a exam
     * @param examDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/exams")
    @Timed
    public ResponseEntity<ExamDTO> createExam(@RequestBody @Valid ExamDTO examDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Exam");
        if (examDTO.getId() != null) {
            throw new WitcurveException("New Exam can't already have an id");
        }
        try {
            ExamDTO result = examService.saveOrUpdate(examDTO);
            return ResponseEntity.created(new URI("/api/exams/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("exams", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * update the given exam
     * @param examDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/exams")
    @Timed
    public ResponseEntity<ExamDTO> updateExam(@RequestBody @Valid ExamDTO examDTO) throws WitcurveException {
        log.debug("Request to update exam");
        if (examDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        } else {
            examService.getExamById(examDTO.getId());
        }
        try {
            ExamDTO result = examService.saveOrUpdate(examDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("exam", examDTO.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * update exam status by id with status
     * @param examId
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/exams/{examId}")
    @Timed
    public ResponseEntity<ExamDTO> updateExamStatus(@PathVariable("examId") Long examId, @RequestParam ExamStatus status) throws WitcurveException {
        log.debug("Request to get Exam with id {}", examId);
        ExamDTO result = examService.updateExamStatus(examId, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get exam by id
     * @param examId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/exams/{examId}")
    @Timed
    public ResponseEntity<ExamDTO> getExamById(@PathVariable("examId") Long examId) throws WitcurveException {
        log.debug("Request to get Exam with id {}", examId);
        ExamDTO result = examService.getExamById(examId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get exam by school info id and grade
     * @param schoolInfoId
     * @param grade
     * @param startDate
     * @param endDate
     * @param status
     *
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/exams/school-info/{schoolInfoId}/grades/{grade}")
    @Timed
    public ResponseEntity<List<ExamDTO>> getExamsBySchoolInfoAndGrade(@PathVariable("schoolInfoId") Long schoolInfoId,
                                                                   @PathVariable("grade") Grade grade,
                                                                      @RequestParam(value = "startDate") LocalDate startDate,
                                                                      @RequestParam(value = "endDate") LocalDate endDate,
                                                                      @RequestParam(value = "status", required = false) ExamStatus status) throws WitcurveException {
        log.debug("Request to get Exams for grade {} in schoolInfoId {}", grade, schoolInfoId);
        List<ExamDTO> result = examService.getExamsBySchoolInfoAndGrade(schoolInfoId, grade, startDate, endDate, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get exam by academic session
     * @param sessionId
     *
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/exams/session/{sessionId}")
    @Timed
    public ResponseEntity<List<ExamDTO>> getExamsBySchoolInfo(@PathVariable("sessionId") Long sessionId) throws WitcurveException {
        log.debug("Request to get Exams for academic session with id : {}", sessionId);
        List<ExamDTO> result = examService.getExamsBySessionId(sessionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the exam
     * @param examId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/exams/{examId}")
    @Timed
    public ResponseEntity<Void> deleteExam(@PathVariable Long examId) throws WitcurveException {
        log.debug("REST request to delete Exam: {}", examId);
        try {
            examService.deleteExam(examId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A exam is deleted with identifier " + examId,
                examId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

}
