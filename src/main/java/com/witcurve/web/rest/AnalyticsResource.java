package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.AnalyticsService;
import com.witcurve.service.dto.SectionPerformanceDTO;
import com.witcurve.service.dto.StudentPerformanceDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.StudentPerformanceDashboardVM;
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
     *
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

    /**
     * get student performance by student id
     *
     * @param studentId
     * @return StudentPerformanceDTO
     * @throws WitcurveException
     */
    @GetMapping("/analytics/student/{studentId}")
    @Timed
    public ResponseEntity<StudentPerformanceDTO> getStudentPerformance(@PathVariable("studentId") Long studentId) throws WitcurveException {
        log.debug("Request to get Student Performance for studentId{}", studentId);
        StudentPerformanceDTO studentPerformanceDTO = analyticsService.getStudentPerformanceByStudentId(studentId);
        return new ResponseEntity<>(studentPerformanceDTO, HttpStatus.OK);
    }

    /**
     * get student performance dashboard details by school inf id
     *
     * @param schoolInfoId
     * @return StudentPerformanceDTO
     * @throws WitcurveException
     */
    @GetMapping("/analytics/student-performance/dashboard/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<StudentPerformanceDashboardVM>> getStudentPerformanceDashboard(@PathVariable("schoolInfoId") Long schoolInfoId) throws WitcurveException {
        log.debug("Request to get Student Performance Dashboard details for school info with id : {}", schoolInfoId);
        List<StudentPerformanceDashboardVM> result = analyticsService.getStudentDashboardDetailsBySchoolInfoId(schoolInfoId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}


