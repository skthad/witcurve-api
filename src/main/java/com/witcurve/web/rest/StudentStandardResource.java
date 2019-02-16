package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentStandardService;
import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentStandardResource {

    private final Logger log = LoggerFactory.getLogger(StudentStandardResource.class);

    @Autowired
    StudentStandardService studentStandardService;

    /**
     * create StudentStandards
     * @return
     * @throws WitcurveException
     */

    @PostMapping("/student-standard/standards/{standardId}/multiple")
    @Timed
    public ResponseEntity<List<StudentStandardDTO>> saveMultipleStudentStandards(@RequestBody List<StudentStandardDTO> studentStandardDTOs,
                                                                                 @PathVariable Long standardId) throws WitcurveException {
        log.debug("Request to add student-standard");

        try {
            List<StudentStandardDTO> result = studentStandardService.saveMultiple(studentStandardDTOs, standardId);
            return ResponseEntity.ok(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("student_standard_UK")) {
                log.error("Unique constraint (student_id, standard_id) violated");
                throw new WitcurveException("There is already student with given standard");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * Creeate StudentStandard
     * @return
     * @throws WitcurveException
     */

    @PostMapping("/student-standard")
    @Timed
    public ResponseEntity<StudentStandardDTO> updateStudentStandard(@RequestBody StudentStandardDTO studentStandardDTO) throws WitcurveException {
        log.debug("Request to add student-standard");

        try {
            StudentStandardDTO result = studentStandardService.save(studentStandardDTO);
            return ResponseEntity.ok(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("student_standard_UK")) {
                log.error("Unique constraint (student_id, standard_id) violated");
                throw new WitcurveException("There is already student with given standard");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

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

    @GetMapping("/student-standard/staff/{staffId}")
    @Timed
    public ResponseEntity<List<StudentStandardDTO>> getByStaffId(@PathVariable("staffId") Long staffId) throws WitcurveException {
        log.debug("Request to get student-standard by schoolInfo id");
        List<StudentStandardDTO> result = studentStandardService.getByStaffId(staffId);
        return ResponseEntity.ok(result);
    }

}
