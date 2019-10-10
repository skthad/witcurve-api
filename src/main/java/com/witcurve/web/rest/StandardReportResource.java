package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.StandardReportService;
import com.witcurve.service.impl.StandardReportDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StandardReportResource {

    private final Logger log = LoggerFactory.getLogger(StandardReportResource.class);

    @Autowired
    StandardReportService standardReportService;

    /**
     * creates a standardReport
     *
     * @param standardReportDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/standard-report")
    @Timed
    public ResponseEntity<StandardReportDTO> createOrUpdateStandardReport(@RequestBody @Valid StandardReportDTO standardReportDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save or update {} :", standardReportDTO);
        StandardReportDTO result = standardReportService.saveOrUpdate(standardReportDTO);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * get standardReport by id
     *
     * @param standardReportId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/standard-report/{standardReportId}")
    @Timed
    public ResponseEntity<StandardReportDTO> getStandardReportById(@PathVariable(value = "standardReportId") Long standardReportId) throws WitcurveException {
        log.debug("Request to get standard report with standardReportId {} :", standardReportId);
        StandardReportDTO result = standardReportService.findById(standardReportId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get standardReport by examId
     *
     * @param examId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/standard-report/exam/{examId}")
    @Timed
    public ResponseEntity<List<StandardReportDTO>> getStandardReportByStudent(@PathVariable("examId") Long examId, @RequestParam(required = false) Grade grade) throws WitcurveException {
        log.debug("Request to get standard reports for exam with id : {} for grade  : {} ",examId, grade);
        List<StandardReportDTO> result = standardReportService.findByExamId(examId, grade);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/standard-report/generate")
    @Timed
    public ResponseEntity<StandardReportDTO> generateStandardReport(@RequestParam Long standardId, @RequestParam Long reportCardId) {
        log.debug("Request to generate report card for standard with id {} with report card id : {}", standardId, reportCardId);
        StandardReportDTO result = standardReportService.generateStandardReport(standardId, reportCardId);
        standardReportService.createReportCards(result);
        return ResponseEntity.ok(result);
    }
}

