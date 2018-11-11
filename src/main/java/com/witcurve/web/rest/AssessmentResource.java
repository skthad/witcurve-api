package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.AssessmentService;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AssessmentResource {

    private final Logger log = LoggerFactory.getLogger(AssessmentResource.class);

    @Autowired
    AssessmentService assessmentService;

    /**
     *
     * @return
     */
    @PostMapping("/assessment/marks")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> enterMarksForTest(
        @RequestBody List<StudentMarksDTO> studentMarks) throws WitcurveException, URISyntaxException {
        log.debug("Request to enter student marks");

        List<StudentMarksDTO> result = assessmentService.enterStudentMarks(studentMarks);

        return new ResponseEntity<>(result,  HttpStatus.OK);
    }

    /**
     *
     * @return
     */
    @GetMapping("/assessment/marks/{testId}")
    @Timed
    public ResponseEntity<List<StudentMarksDTO>> enterMarksForTest(
        @RequestParam(value = "studentId", required = false) Long studentId,
        @PathVariable Long testId) throws WitcurveException, URISyntaxException {
        log.debug("Request to get student marks for testId = {}, studentId = {}", testId, studentId);

        List<StudentMarksDTO> result;
        if (studentId == null) {
            result = assessmentService.getStudentMarksByTestId(testId);
        } else {
            result = assessmentService.getStudentMarksByStudentAndTestId(studentId, testId);
        }

        return new ResponseEntity<>(result,  HttpStatus.OK);
    }

}
