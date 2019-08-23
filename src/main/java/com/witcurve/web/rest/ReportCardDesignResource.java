package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.domain.enumeration.ReportModelType;
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
    @PostMapping("/report-card-designs")
    @Timed
    public ResponseEntity<List<ReportCardDesignDTO>> createReportCardDesigns(@RequestBody @Valid List<ReportCardDesignDTO> reportCardDesignDTOS, @RequestParam Long schoolInfoId) throws WitcurveException, URISyntaxException {
        log.debug("Request to save or update reportCardDesigns : {}",reportCardDesignDTOS);
        try {
            List<ReportCardDesignDTO> result = reportCardDesignService.saveOrUpdate(reportCardDesignDTOS, schoolInfoId);
            return ResponseEntity.created(new URI("/api/report-card-designs/"))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            throw new WitcurveException("DataIntegrityViolationException occurred.");
        }
    }

    /**
     * get reportCardDesigns by schoolInfoId and modelType
     * @param schoolInfoId
     * @param modelType
     * @param fieldType
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/report-card-designs/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<ReportCardDesignDTO>> getReportCardDesigns(@PathVariable Long schoolInfoId, @RequestParam ReportModelType modelType, @RequestParam(required = false)ReportFieldType fieldType) throws WitcurveException {
        log.debug("Request to get ReportCardDesign of model type :{} and field type : {} for school info with id : {}", schoolInfoId);
        List<ReportCardDesignDTO> result = reportCardDesignService.findByModelTypeAndSchoolInfoId(modelType, schoolInfoId, fieldType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get manual entry reportCardDesigns by event or ecd id
     * @param modelType
     * @param id
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/report-card-designs/model-type/{modelType}")
    @Timed
    public ResponseEntity<List<ReportCardDesignDTO>> getManualEntryReportCardDesignsForEventOrEcdId(@PathVariable ReportModelType modelType, @RequestParam Long id) throws WitcurveException {
        log.debug("Request to get ReportCardDesign of model type : {} with id : {}", modelType, id);
        List<ReportCardDesignDTO> result = reportCardDesignService.findManualEntryFieldsByEcdIdOrEventId(modelType, id);
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
     * deactivate reportCardDesigns by ids
     * @param ids
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/report-card-designs")
    @Timed
    public ResponseEntity<Void> deactivateReportCard(@RequestParam List<Long> ids) throws WitcurveException {
        log.debug("Request to get ReportCardDesign with ids : {}", ids);
        reportCardDesignService.deleteReportCardDesign(ids);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
