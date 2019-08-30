package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentRemarksService;
import com.witcurve.service.dto.StudentRemarksDTO;
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
public class StudentRemarksResource {

    private final Logger log = LoggerFactory.getLogger(StudentRemarksService.class);

    @Autowired
    StudentRemarksService studentRemarksService;


    /**
     * creates a new student remarks
     * @param studentRemarksDTOs
     * @param examId
     * @param bindingId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/student-remarks")
    @Timed
    public ResponseEntity<List<StudentRemarksDTO>> createStudentRemarks(@RequestBody @Valid List<StudentRemarksDTO> studentRemarksDTOs,
                                                                    @RequestParam(required = false) Long examId,
                                                                    @RequestParam(required = false) String bindingId) throws WitcurveException, URISyntaxException {
        log.debug("Request to create student Remarks ");
        List<StudentRemarksDTO> result = studentRemarksService.saveOrUpdate(studentRemarksDTOs, examId, bindingId);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * get studentRemarkDTOs by examId or by bindingId
     * @param examId
     * @param bindingId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/student-remarks")
    @Timed
    public ResponseEntity<List<StudentRemarksDTO>> getStudentRemarks(@RequestParam(required = false) Long examId, @RequestParam(required = false) String bindingId) throws WitcurveException {
        log.debug("Request to get student remarks for exam with id : {} or periodic test with binding id : {}", examId, bindingId);
        List<StudentRemarksDTO> result = studentRemarksService.findByExamIdOrBindingId(examId, bindingId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete student-remarks by ids
     * @param ids
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/student-remarks")
    @Timed
    public ResponseEntity<Void> deleteStudentRemarks(@RequestParam List<Long> ids) throws WitcurveException {
        log.debug("Request to delete StudentRemarks with ids : {} ", ids);
        studentRemarksService.deleteStudentMarks(ids);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }



}
