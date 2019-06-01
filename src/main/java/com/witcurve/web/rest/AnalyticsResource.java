package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.AnalyticsService;
import com.witcurve.service.dto.SectionPerformanceDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing the analytics.
 */
@RestController
@RequestMapping("/api")
public class AnalyticsResource {

    private final Logger log = LoggerFactory.getLogger(AnalyticsResource.class);

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * get section performance by exam id
     * @param examId
     * @return list of section performance
     * @throws WitcurveException
     */
    @GetMapping("/analytics/student/section")
    @Timed
    public ResponseEntity<List<SectionPerformanceDTO>> getSectionPerformance(@RequestParam Long examId) throws WitcurveException {
        log.debug("Request to get Student Performance for and examId {}", examId);
        List<SectionPerformanceDTO> sectionPerformanceDTOList = analyticsService.getSectionPerformance(examId);
        return new ResponseEntity<>(sectionPerformanceDTOList, HttpStatus.OK);
    }

}
