package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentCourseService;
import com.witcurve.service.dto.StudentCourseDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentCourseResource {

    private final Logger log = LoggerFactory.getLogger(StudentCourseResource.class);

    @Autowired
    StudentCourseService studentCourseService;

    /**
     * Creeate StudentCourses
     * @return
     * @throws WitcurveException
     */

    @PostMapping("/student-course")
    @Timed
    public ResponseEntity<List<StudentCourseDTO>> updateStudentStandard(@Valid @RequestBody List<StudentCourseDTO> studentCourseDTOs) throws WitcurveException {
        log.debug("Request to create or update studentCourses : {}", studentCourseDTOs);
        try {
            List<StudentCourseDTO> result = studentCourseService.saveOrUpdate(studentCourseDTOs);
            return ResponseEntity.ok(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("student_course_unique_UK")) {
                log.error("Unique constraint (student_standard_id, course_id) violated");
                throw new WitcurveException("There is already student standard and course combination");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    @GetMapping("/student-course/selective/student/{studentId}")
    @Timed
    public ResponseEntity<List<StudentCourseDTO>> getStudentCoursesByStudentId(@PathVariable Long studentId) {
        log.debug("Request to get studentCourses by student with id : {}", studentId);
        List<StudentCourseDTO> result = studentCourseService.getSelectiveCoursesByStudent(studentId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/student-course/selective/standard/{standardId}")
    @Timed
    public ResponseEntity<List<StudentCourseDTO>> getStudentCoursesByStandardId(@PathVariable Long standardId) {
        log.debug("Request to get studentCourses by standard with id : {}", standardId);
        List<StudentCourseDTO> result = studentCourseService.getSelectiveCoursesByStandard(standardId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/student-course")
    @Timed
    public ResponseEntity<Void> deactivateStudentCourseByIds(@RequestParam List<Long> ids) {
        log.debug("Request to deactivate studentCourses by ids : {}", ids);
        studentCourseService.deactivateStudentCourseByIds(ids);
        return ResponseEntity.ok(null);
    }


}
