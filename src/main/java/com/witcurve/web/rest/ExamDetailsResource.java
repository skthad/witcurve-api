package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.ExamDetailsService;
import com.witcurve.service.dto.ExamDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class ExamDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ExamDetailsResource.class);

    @Autowired
    ExamDetailsService examDetailsService;

    /**
     * creates a ExamDetails
     * @param examDetailsDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/exam-details")
    @Timed
    public ResponseEntity<ExamDetailsDTO> createExamDetails(@RequestBody @Valid ExamDetailsDTO examDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save ExamDetails");
        if (examDetailsDTO.getId() != null) {
            throw new WitcurveException("New ExamDetails can't already have an id");
        }
        ExamDetailsDTO result = examDetailsService.saveOrUpdate(examDetailsDTO);
        return ResponseEntity.created(new URI("/api/exam-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("examDetails", result.getId().toString()))
            .body(result);
    }

    /**
     * get ExamDetails by id
     * @param examDetailsId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/exam-details/{examDetailsId}")
    @Timed
    public ResponseEntity<ExamDetailsDTO> getExamDetailsById(@PathVariable("examDetailsId") Long examDetailsId) throws WitcurveException {
        log.debug("Request to get ExamDetails with id {}", examDetailsId);
        ExamDetailsDTO result = examDetailsService.getExamDetailsById(examDetailsId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given examDetails
     * @param examDetailsDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/exam-details")
    @Timed
    public ResponseEntity<ExamDetailsDTO> updateExamDetails(@RequestBody @Valid ExamDetailsDTO examDetailsDTO) throws WitcurveException {
        log.debug("Request to update ExamDetails");
        if (examDetailsDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        ExamDetailsDTO result = examDetailsService.saveOrUpdate(examDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("examDetails", examDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the examDetails
     * @param examDetailsId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/exam-details/{examDetailsId}")
    @Timed
    public ResponseEntity<Void> deleteExamDeatils(@PathVariable Long examDetailsId) throws WitcurveException {
        log.debug("REST request to delete ExamDetails: {}", examDetailsId);
        examDetailsService.deleteExamDetails(examDetailsId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("An exam details is deleted with identifier " + examDetailsId,
            examDetailsId.toString())).build();
    }
}
