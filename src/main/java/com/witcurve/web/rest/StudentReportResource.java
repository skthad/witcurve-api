package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentReportService;
import com.witcurve.service.dto.StudentReportDTO;
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
public class StudentReportResource {

    private final Logger log = LoggerFactory.getLogger(StudentReportResource.class);

    @Autowired
    StudentReportService studentReportService;

    /**
     * creates a studentReport
     *
     * @param studentReportDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/student-report")
    @Timed
    public ResponseEntity<StudentReportDTO> createOrUpdateStudentReport(@RequestBody @Valid StudentReportDTO studentReportDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save or update {} :", studentReportDTO);
        StudentReportDTO result = studentReportService.saveOrUpdateStudentReport(studentReportDTO);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * get studentReport by id
     *
     * @param studentReportId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/student-report/{studentReportId}")
    @Timed
    public ResponseEntity<StudentReportDTO> getStudentReportById(@PathVariable(value = "studentReportId") Long studentReportId) throws WitcurveException {
        log.debug("Request to get studentRequest with studentReportId {} :", studentReportId);
        StudentReportDTO result = studentReportService.getStudentReportById(studentReportId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get studentReport by studentId
     *
     * @param studentId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/student-report/student/{studentId}")
    @Timed
    public ResponseEntity<List<StudentReportDTO>> getStudentReportByStudent(@PathVariable("studentId") Long studentId, @RequestParam(required = false) Long reportCardId) throws WitcurveException {
        log.debug("Request to get studentRequest with studentId : {} for report card : {} ", studentId, reportCardId);
        List<StudentReportDTO> result = studentReportService.getStudentReportByStudentId(studentId, reportCardId);
        return ResponseEntity.ok(result);

    }

    /**
     * delete the studentReport
     *
     * @param studentReportId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/student-report/{studentReportId}")
    @Timed
    public ResponseEntity<Void> deleteStudentReport(@PathVariable Long studentReportId) throws WitcurveException {
        log.debug("REST request to delete studentReport: {}", studentReportId);
        studentReportService.deleteStudentReportById(studentReportId);
        return ResponseEntity.ok(null);
    }
}

