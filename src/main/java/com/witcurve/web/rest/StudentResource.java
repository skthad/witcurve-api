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
            if (e.getMessage().contains("admission_school_info_id_UK") || e.getMessage().contains("UC_WC_USERLOGIN_COL")) {
               log.error("Unique constraint (admission_id, school_info_id) violated");
               throw new WitcurveException("There already a student with given admission id for this board");
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
        } else {
            studentService.getStudentById(studentDTO.getId());
        }
        if (studentDTO.getUserId() == null) {
            throw new WitcurveException("User Id is missing for this student");
        }
        try {
            StudentDTO result = studentService.update(studentDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("student", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("admission_school_info_id_UK") || e.getMessage().contains("UC_WC_USERLOGIN_COL")) {
                log.error("Unique constraint (admission_id, school_info_id) violated");
                throw new WitcurveException("There already a student with given admission id for this board");
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

    @GetMapping("/students/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<StudentDTO>> getStudentsBySchoolInfoId(@PathVariable("schoolInfoId") Long schoolInfoId, @RequestParam(defaultValue = "true") Boolean activated) throws WitcurveException {
        log.debug("Request to get unallocated students in schoolInfo with id: {} of active status : {}", schoolInfoId, activated);
        List<StudentDTO> result;
        if(activated) {
            result = studentService.getUnAllocatedStudentsBySchoolInfoId(schoolInfoId);
        } else {
            result = studentService.getInActiveStudentsBySchoolInfoId(schoolInfoId);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<Void> deactivateStudent(@PathVariable Long studentId) throws WitcurveException, URISyntaxException {
        log.debug("Request to deactivate student with ID: " + studentId);
        studentService.deactivate(studentId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("A student is deactivated with identifier " + studentId,
            studentId.toString())).build();
    }

    @PatchMapping("/students/{studentId}/activate")
    public ResponseEntity<Void> activateStudent(@PathVariable Long studentId) throws WitcurveException, URISyntaxException {
        log.debug("Request to activate student with ID: " + studentId);
        studentService.activate(studentId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("A student is deactivated with identifier " + studentId,
            studentId.toString())).build();
    }

    @PostMapping("/students/courses")
    @Timed
    public ResponseEntity<Void> mapUnmapStudentToCourse(@RequestParam Long studentStandardId,
                                                        @RequestParam Long courseId,
                                                        @RequestParam(required = false, defaultValue = "true") Boolean map) throws WitcurveException {
        log.debug(String.format("Request to {} student to course"), (map ? "map": "unmap"));

        try {
            studentService.mapUnmapStudentAndCourse(studentStandardId, courseId, map);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Mapping/Unmapping done ",
                null)).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("student_course_unique_UK")) {
                log.error("Unique constraint (student_standard_id, course_id) violated");
                throw new WitcurveException("The student is already assigned to the intended course");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    @PostMapping("/students/courses/map")
    @Timed
    public ResponseEntity<Void> mapOneTime(@RequestParam Long schoolInfoId) throws WitcurveException {
        log.debug("Request to map students to all courses");

        studentService.mapOneTime(schoolInfoId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("One time mapping done ",
            null)).build();
    }

}
