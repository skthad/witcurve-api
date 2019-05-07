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
import org.springframework.cglib.core.Local;
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
            throw new WitcurveException("DataIntegrityViolationException occurred.");
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
        }
        try {
            ExamDTO result = examService.saveOrUpdate(examDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("exam", examDTO.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            throw new WitcurveException("DataIntegrityViolationException occurred.");
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
     * get exam between fromDate and endDate for school board with schoolInfoId
     * @param schoolInfoId
     * @param fromDate
     * @param endDate
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/exams/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<ExamDTO>> getExams(@PathVariable Long schoolInfoId, @RequestParam(required = false) Grade grade, @RequestParam LocalDate fromDate,
                                                  @RequestParam LocalDate endDate, @RequestParam(required = false) List<ExamStatus> statusList) throws WitcurveException {
        log.debug("Request to get Exams between dates {} and {} for school info with id : {} of grade : {} and of status : {}", fromDate, endDate, schoolInfoId, grade, statusList);
        List<ExamDTO> result = examService.getExamsBetweenDates(schoolInfoId, fromDate, endDate, grade, statusList);
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
        examService.deleteExam(examId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A exam is deleted with identifier " + examId,
            examId.toString())).build();


    }

}
