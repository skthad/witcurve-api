package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentService;
import com.witcurve.service.dto.StudentDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/student")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request create student");
        if (studentDTO.getId() != null) {
            throw new WitcurveException("New student can't already have an id");
        }
        StudentDTO result = studentService.create(studentDTO);
        return ResponseEntity.created(new URI("/api/student/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("student", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/student")
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody @Valid StudentDTO studentDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request create standard");
        if (studentDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        StudentDTO result = studentService.update(studentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("student", result.getId().toString()))
            .body(result);
    }

    @GetMapping("/student/{studentId}")
    @Timed
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable("studentId") Long studentId) throws WitcurveException {
        log.debug("Request to get student by id");
        StudentDTO result = studentService.getStudentById(studentId);
        return ResponseEntity.ok(result);
    }

    /**
     * get Student by school id
     * @param schoolId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/student/schools/{schoolId}")
    @Timed
    public ResponseEntity<List<StudentDTO>> getStudentBySchoolId(@PathVariable("schoolId") Long schoolId) throws WitcurveException {
        log.debug("Request to get students with school id {}", schoolId);
        List<StudentDTO> result = studentService.getStudentsBySchoolId(schoolId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/students/standard/{standardId}")
    @Timed
    public ResponseEntity<List<StudentDTO>> getStudentsByStandardId(@PathVariable("standardId") Long standardId) throws WitcurveException {
        log.debug("Request to get students by standard id");
        List<StudentDTO> result = studentService.getStudentsByStandardId(standardId);
        return ResponseEntity.ok(result);
    }

    /**
     * delete the student
     * @param studentId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/student/{studentId}")
    @Timed
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) throws WitcurveException {
        log.debug("REST request to delete Student: {}", studentId);
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A student is deleted with identifier " + studentId,
            studentId.toString())).build();
    }

}
