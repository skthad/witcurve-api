package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Function;
import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.ReportCardService;
import com.witcurve.service.dto.ReportCardDTO;
import com.witcurve.service.util.WitCurveConstants;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.ReportCardVM;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReportCardResource {

    private final Logger log = LoggerFactory.getLogger(ReportCardResource.class);

    @Autowired
    ReportCardService reportCardService;

    @PostMapping("/report-card/preview")
    public ResponseEntity<Resource> getReportCardTemplate(@Valid @RequestBody ReportCardVM reportCardVM) {
        log.debug("Request to get report card pdf template with details : {}", reportCardVM);
        File result = reportCardService.getReportCardTemplatePdf(reportCardVM, WitCurveConstants.EXAM_PERIODIC_REPPORT_CARD_TEMPLATE, null);
        Resource resource = WitcurveUtil.getResourceFromFile(result);
        return ResponseEntity.ok(resource);


    }

    @PostMapping("/report-card/template-html")
    public ResponseEntity<Resource> getReportCardTemplateHtmlFile(@Valid @RequestBody ReportCardVM reportCardVM) {
        log.debug("Request to get report card html template with details : {}", reportCardVM);
        File result = reportCardService.getReportCardTemplateHtml(reportCardVM, WitCurveConstants.EXAM_PERIODIC_REPPORT_CARD_TEMPLATE, null);
        Resource resource = WitcurveUtil.getResourceFromFile(result);
        return ResponseEntity.ok(resource);
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
     * get reportCard preview by reportCardId and standardId
     * @param reportCardId
     * @param standardId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/report-card/{reportCardId}/preview")
    @Timed
    public ResponseEntity<Page<String>> getReportCardsForExamIdStandardId(@PathVariable Long reportCardId, @RequestParam Long standardId,
                                                                          @RequestParam(defaultValue = "true") Boolean showHeader,
                                                                          @RequestParam(defaultValue = "pdf") String type,
                                                                          @ApiParam Pageable pageable) throws WitcurveException {
        log.debug("Request to get reportCardDesigns with report card with id : {} and for standard with id : {}", reportCardId, standardId);
        Page<File> fileList = reportCardService.getReportCardPreviewForStandard(reportCardId, standardId, pageable, showHeader, type);
        Page<String> result = fileList.map(new Function<File, String>() {
            @Override
            public String apply(File file) {
                try {
                    byte[] fileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                    String result = new String(Base64.getEncoder().encode(fileBytes));
                    return result;
                } catch (IOException e) {
                    return "Error: File Corrupted";
                }
            }
        });
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

    @GetMapping("/report-card/grade-details/exam/{examId}")
    @Timed
    public ResponseEntity<Map<String,String>> getGradeDetailsByExamIdAndConfigType(@PathVariable Long examId, @RequestParam ConfigType configType) throws WitcurveException {
        log.debug("Request to get reportCardDesigns for configType : {} and for exam with id : {}", configType, examId);
        Map<String,String> result = reportCardService.getGradeDetailsByExamIdAndConfigType(examId,configType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
