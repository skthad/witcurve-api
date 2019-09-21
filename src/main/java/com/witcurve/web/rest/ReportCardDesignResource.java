package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.service.ReportCardDesignService;
import com.witcurve.service.dto.ReportCardDesignDTO;
import com.witcurve.web.rest.errors.WitcurveException;
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
public class ReportCardDesignResource {

    private final Logger log = LoggerFactory.getLogger(ReportCardDesignResource.class);

    @Autowired
    ReportCardDesignService reportCardDesignService;

    /**
     * creates or updates a reportCardDesigns
     * @param reportCardDesignDTOS
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/report-card-designs/grade/{grade}")
    @Timed
    public ResponseEntity<List<ReportCardDesignDTO>> createReportCardDesigns(@RequestBody @Valid List<ReportCardDesignDTO> reportCardDesignDTOS, @PathVariable Grade grade,  @RequestParam Long examId) throws WitcurveException, URISyntaxException {
        log.debug("Request to save or update reportCardDesigns : {} for exam with id : {} for grade : {}",reportCardDesignDTOS, examId, grade);
        try {
            List<ReportCardDesignDTO> result = reportCardDesignService.saveOrUpdate(reportCardDesignDTOS, examId, grade);
            return ResponseEntity.created(new URI("/api/report-card-designs/"))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            throw new WitcurveException("DataIntegrityViolationException occurred.");
        }
    }

    /**
     * get reportCardDesigns by examId or by bindingId
     * @param examId
     * @param bindingId
     * @param fieldType
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/report-card-designs/grade/{grade}")
    @Timed
    public ResponseEntity<List<ReportCardDesignDTO>> getReportCardDesigns(@PathVariable Grade grade,  @RequestParam Long examId,  @RequestParam(required = false)ReportFieldType fieldType) throws WitcurveException {
        log.debug("Request to get reportCardDesigns of field type : {} for exam with id : {}", fieldType, examId);
        List<ReportCardDesignDTO> result = reportCardDesignService.findByExamIdOrBindingIdWithFieldType(grade, examId, fieldType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get reportCardDesign by id
     * @param id
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/report-card-designs/{id}")
    @Timed
    public ResponseEntity<ReportCardDesignDTO> getManualEntryReportCardDesignsById(@PathVariable Long id) throws WitcurveException {
        log.debug("Request to get ReportCardDesign with id : {}", id);
        ReportCardDesignDTO result = reportCardDesignService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete reportCardDesigns by ids
     * @param ids
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/report-card-designs")
    @Timed
    public ResponseEntity<Void> deleteReportCardDesign(@RequestParam List<Long> ids) throws WitcurveException {
        log.debug("Request to delete ReportCardDesign with ids : {} ", ids);
        reportCardDesignService.deleteReportCardDesign(ids);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
