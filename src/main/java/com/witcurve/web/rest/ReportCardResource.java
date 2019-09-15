package com.witcurve.web.rest;

import com.witcurve.service.ReportCardService;
import com.witcurve.service.util.WitCurveConstants;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.ReportCardVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportCardResource {

    private final Logger log = LoggerFactory.getLogger(ReportCardResource.class);

    @Autowired
    ReportCardService reportCardService;

    @GetMapping("/report-card/preview")
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

    @GetMapping("/report-card/template-html")
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

    @GetMapping("/report-card/preview-details/standard/{standardId}")
    public ResponseEntity<List<ReportCardVM>> getReportCardDetailsForStandard(@PathVariable Long standardId, @RequestParam(required = false) Long examId, @RequestParam(required = false) String bindingId) {
        log.debug("Request to get reportCardVM list for standard with id : {} for exam with id : {} or periodic test with bindingId : {}", standardId, examId, bindingId);
        List<ReportCardVM> result = reportCardService.getReportCardDetailsForStandard(standardId, examId, bindingId);
        return ResponseEntity.ok(result);
    }
}
