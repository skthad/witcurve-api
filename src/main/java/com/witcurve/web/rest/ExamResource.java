package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
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
    @PostMapping("/exam")
    @Timed
    public ResponseEntity<ExamDTO> createExam(@RequestBody @Valid ExamDTO examDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Exam");
        if (examDTO.getId() != null) {
            throw new WitcurveException("New Exam can't already have an id");
        }
        ExamDTO result = examService.saveOrUpdate(examDTO);
        return ResponseEntity.created(new URI("/api/exam/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("exam", result.getId().toString()))
            .body(result);
    }

    /**
     * get exam by id
     * @param examId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/exam/{examId}")
    @Timed
    public ResponseEntity<ExamDTO> getExamById(@PathVariable("examId") Long examId) throws WitcurveException {
        log.debug("Request to get Exam with id {}", examId);
        ExamDTO result = examService.getExamById(examId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given exam
     * @param examDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/exam")
    @Timed
    public ResponseEntity<ExamDTO> updateExam(@RequestBody @Valid ExamDTO examDTO) throws WitcurveException {
        log.debug("Request to update exam");
        if (examDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        } else {
            examService.getExamById(examDTO.getId());
        }
        ExamDTO result = examService.saveOrUpdate(examDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("exam", examDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the exam
     * @param examId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/exam/{examId}")
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
