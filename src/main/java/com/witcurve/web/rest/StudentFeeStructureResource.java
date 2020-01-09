package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentFeeStructureService;
import com.witcurve.service.dto.StudentFeeStructureDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentFeeStructureResource {

    private final Logger log = LoggerFactory.getLogger(StudentFeeStructureResource.class);

    @Autowired
    StudentFeeStructureService studentFeeStructureService;

    /**
     * save studentFeeStructure
     *
     * @return
     * @throws WitcurveException
     */

    @PostMapping("/student-fee-structure")
    @Timed
    public ResponseEntity<StudentFeeStructureDTO> saveStudentFeeStructure(@Valid @RequestBody StudentFeeStructureDTO studentFeeStructureDTO) throws WitcurveException {
        log.debug("Request to create StudentFeeStructure : {}", studentFeeStructureDTO);
        if (studentFeeStructureDTO.getId() != null) {
            throw new WitcurveException("New record can not have id already");
        }
        try {
            StudentFeeStructureDTO result = studentFeeStructureService.saveOrUpdate(studentFeeStructureDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("student_session_id_UK")) {
                throw new WitcurveException("Unique constraint (student_id, session_id) violated");
            } else if (e.getMessage().contains("student_fee_structure_id_fee_type_id_UK")) {
                throw new WitcurveException("Fee type id should be unique for a student fee structure");
            } else if (e.getMessage().contains("student_fee_type_id_fee_description_id_UK")) {
                throw new WitcurveException("Fee description id should be unique for a student fee type");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * update studentFeeStructure
     *
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/student-fee-structure")
    @Timed
    public ResponseEntity<StudentFeeStructureDTO> updateStudentFeeStructure(@Valid @RequestBody StudentFeeStructureDTO studentFeeStructureDTO) throws WitcurveException {
        log.debug("Request to update StudentFeeStructure : {}", studentFeeStructureDTO);
        if (studentFeeStructureDTO.getId() == null) {
            throw new WitcurveException("Id is require to update record");
        }
        try {
            StudentFeeStructureDTO result = studentFeeStructureService.saveOrUpdate(studentFeeStructureDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("student_session_id_UK")) {
                throw new WitcurveException("Unique constraint (student_id, session_id) violated");
            } else if (e.getMessage().contains("student_fee_structure_id_fee_type_id_UK")) {
                throw new WitcurveException("Fee type id should be unique for a student fee structure");
            } else if (e.getMessage().contains("student_fee_type_id_fee_description_id_UK")) {
                throw new WitcurveException("Fee description id should be unique for a student fee type");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get studentFeeStructure
     *
     * @param studentId
     * @param sessionId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/student-fee-structure/students/{studentId}/academic-session/{sessionId}")
    @Timed
    public ResponseEntity<StudentFeeStructureDTO> getByStudentAndSessionId(@PathVariable Long studentId, @PathVariable Long sessionId) throws WitcurveException {
        log.debug("Request to get StudentFeeStructure by studentId and sessionId : {}", studentId, sessionId);
        StudentFeeStructureDTO result = studentFeeStructureService.getByStudentIdAndSessionId(studentId, sessionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get studentFeeStructure
     *
     * @param standardId
     * @param sessionId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/student-fee-structure/standards/{standardId}/academic-session/{sessionId}")
    @Timed
    public ResponseEntity<List<StudentFeeStructureDTO>> getByStandardAndSessionId(@PathVariable Long standardId, @PathVariable Long sessionId) throws WitcurveException {
        log.debug("Request to get StudentFeeStructure by standardId and sessionId : {}", standardId, sessionId);
        List<StudentFeeStructureDTO> result = studentFeeStructureService.getByStandardIdAndSessionId(standardId, sessionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
