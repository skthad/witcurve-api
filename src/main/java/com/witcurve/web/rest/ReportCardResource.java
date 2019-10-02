package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.service.ReportCardService;
import com.witcurve.service.dto.ReportCardDTO;
import com.witcurve.service.dto.ReportCardDesignDTO;
import com.witcurve.service.util.WitCurveConstants;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.ReportCardVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportCardResource {

    private final Logger log = LoggerFactory.getLogger(ReportCardResource.class);

    @Autowired
    ReportCardService reportCardService;

    @PostMapping("/report-card/preview")
    public ResponseEntity<Resource> getReportCardTemplate(@Valid @RequestBody ReportCardVM reportCardVM) {
        log.debug("Request to get report card pdf template with details : {}", reportCardVM);
        try {
            File result = reportCardService.getReportCardTemplatePdf(reportCardVM, WitCurveConstants.EXAM_PERIODIC_REPPORT_CARD_TEMPLATE);
            Resource  resource = new InputStreamResource(new FileInputStream(result));
            return ResponseEntity.ok(resource);
        } catch (IOException e) {
            log.debug("Error while reading contents : {}",e.getMessage());
            throw new WitcurveException("There was a problem generating pdf preview");
        }

    }

    @PostMapping("/report-card/template-html")
    public ResponseEntity<Resource> getReportCardTemplateHtmlFile(@Valid @RequestBody ReportCardVM reportCardVM) {
        log.debug("Request to get report card html template with details : {}", reportCardVM);
        try {
            File result = reportCardService.getReportCardTemplateHtml(reportCardVM, WitCurveConstants.EXAM_PERIODIC_REPPORT_CARD_TEMPLATE);
            Resource  resource = new InputStreamResource(new FileInputStream(result));
            return ResponseEntity.ok(resource);
        } catch (IOException e) {
            log.debug("Error while reading contents : {}",e.getMessage());
            throw new WitcurveException("There was a problem generating html preview");
        }
    }

    /**
     * creates or updates a reportCard
     * @param reportCardDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/report-card")
    @Timed
    public ResponseEntity<ReportCardDTO> createReportCardDesigns(@RequestBody @Valid ReportCardDTO reportCardDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to save or update reportCard : {} ",reportCardDTO);
        try {
            ReportCardDTO result = reportCardService.saveOrUpdate(reportCardDTO);
            return ResponseEntity.created(new URI("/api/report-card/")).body(result);
        } catch (DataIntegrityViolationException e) {
            throw new WitcurveException("DataIntegrityViolationException occurred.");
        }
    }

    /**
     * get reportCards by examId and grade
     * @param examId
     * @param grade
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/report-card/exam/{examId}")
    @Timed
    public ResponseEntity<List<ReportCardDTO>> getReportCardsByExamId(@PathVariable Long examId, @RequestParam(required = false) Grade grade) throws WitcurveException {
        log.debug("Request to get reportCardDesigns for grade : {} and for exam with id : {}", grade, examId);
        List<ReportCardDTO> result = reportCardService.getReportCardByExam(examId, grade);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get reportCard by id
     * @param id
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/report-card/{id}")
    @Timed
    public ResponseEntity<ReportCardDTO> getReportCardById(@PathVariable Long id) throws WitcurveException {
        log.debug("Request to get ReportCard with id : {}", id);
        ReportCardDTO result = reportCardService.findOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/report-card/preview-details/standard/{standardId}")
    public ResponseEntity<List<ReportCardVM>> getReportCardDetailsForStandard(@PathVariable Long standardId, @RequestParam(required = false) Long examId, @RequestParam(required = false) String bindingId) {
        log.debug("Request to get reportCardVM list for standard with id : {} for exam with id : {} or periodic test with bindingId : {}", standardId, examId, bindingId);
        List<ReportCardVM> result = null;
        return ResponseEntity.ok(result);
    }
}
