package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentPerformanceService;
import com.witcurve.service.dto.StudentPerformanceDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StudentPerformanceResource {

    private final Logger log = LoggerFactory.getLogger(StudentPerformanceResource.class);

    @Autowired
    StudentPerformanceService studentPerformanceService;

    /**
     * get student performance by exam id
     *
     * @param studentId
     * @return StudentPerformanceDTO
     * @throws WitcurveException
     */
    @GetMapping("/analytics/student/{studentId}")
    @Timed
    public ResponseEntity<StudentPerformanceDTO> getStudentPerformance(@PathVariable("studentId") Long studentId) throws WitcurveException {
        log.debug("Request to get Student Performance for studentId{}", studentId);
        StudentPerformanceDTO studentPerformanceDTO = studentPerformanceService.getStudentPerformanceByStudentId(studentId);
        return new ResponseEntity<>(studentPerformanceDTO, HttpStatus.OK);
    }
}
