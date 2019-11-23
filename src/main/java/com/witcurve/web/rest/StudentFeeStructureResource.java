package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentFeeStructureService;
import com.witcurve.service.dto.StudentFeeStructureDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        StudentFeeStructureDTO result = studentFeeStructureService.saveOrUpdate(studentFeeStructureDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
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
        if(studentFeeStructureDTO.getId() == null){
            throw new WitcurveException("Id is require to update record");
        }
        StudentFeeStructureDTO result = studentFeeStructureService.saveOrUpdate(studentFeeStructureDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
