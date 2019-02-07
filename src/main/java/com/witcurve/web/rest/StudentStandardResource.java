package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentStandardService;
import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentStandardResource {

    private final Logger log = LoggerFactory.getLogger(StudentStandardResource.class);

    @Autowired
    StudentStandardService studentStandardService;

    @GetMapping("/student-standard/students/{studentId}")
    @Timed
    public ResponseEntity<StudentStandardDTO> getByStudentId(@PathVariable("studentId") Long studentId) throws WitcurveException {
        log.debug("Request to get student-standard by student id");
        StudentStandardDTO result = studentStandardService.getByStudentId(studentId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/student-standard/standards/{standardId}")
    @Timed
    public ResponseEntity<List<StudentStandardDTO>> getByStandardId(@PathVariable("standardId") Long standardId) throws WitcurveException {
        log.debug("Request to get student-standard by standard id");
        List<StudentStandardDTO> result = studentStandardService.getByStandardId(standardId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/student-standard/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<StudentStandardDTO>> getBySchoolInfoId(@PathVariable("schoolInfoId") Long schoolInfoId) throws WitcurveException {
        log.debug("Request to get student-standard by schoolInfo id");
        List<StudentStandardDTO> result = studentStandardService.getBySchoolInfoId(schoolInfoId);
        return ResponseEntity.ok(result);
    }

}
