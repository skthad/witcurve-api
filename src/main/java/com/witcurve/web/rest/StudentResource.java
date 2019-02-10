package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentService;
import com.witcurve.service.dto.StudentDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentResource {

    private final Logger log = LoggerFactory.getLogger(StudentResource.class);

    @Autowired
    StudentService studentService;

    @PostMapping("/students")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to create student");
        if (studentDTO.getId() != null) {
            throw new WitcurveException("New student can't already have an id");
        }
        try {
            StudentDTO result = studentService.create(studentDTO);
            return ResponseEntity.created(new URI("/api/students/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("student", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("admission_school_info_id_UK")) {
                throw new WitcurveException("Unique constraint (admission_id, school_info_id) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    @PutMapping("/students")
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody @Valid StudentDTO studentDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request create standard");
        if (studentDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        try {
            StudentDTO result = studentService.update(studentDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("student", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("admission_school_info_id_UK")) {
                throw new WitcurveException("Unique constraint (admission_id, school_info_id) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    @GetMapping("/students/{studentId}")
    @Timed
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable("studentId") Long studentId) throws WitcurveException {
        log.debug("Request to get student by id");
        StudentDTO result = studentService.getStudentById(studentId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/students/standards/{standardId}")
    @Timed
    public ResponseEntity<List<StudentDTO>> getStudentsByStandardId(@PathVariable("standardId") Long standardId) throws WitcurveException {
        log.debug("Request to get students by standard id");
        List<StudentDTO> result = studentService.getStudentsByStandardId(standardId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/students/{studentId}/deactivate")
    public ResponseEntity<Void> deactivateStudent(@PathVariable Long studentId) throws WitcurveException, URISyntaxException {
        log.debug("Request to deactivate student with ID: " + studentId);
        studentService.deactivate(studentId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("A student is deactivated with identifier " + studentId,
            studentId.toString())).build();
    }

}
