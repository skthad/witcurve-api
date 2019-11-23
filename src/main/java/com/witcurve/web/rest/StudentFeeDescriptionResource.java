package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.StudentFeeDescription;
import com.witcurve.service.StudentFeeDescriptionService;
import com.witcurve.service.dto.StudentFeeDescriptionDTO;
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
public class StudentFeeDescriptionResource {

    private final Logger log = LoggerFactory.getLogger(StudentFeeDescriptionResource.class);

    @Autowired
    StudentFeeDescriptionService studentFeeDescriptionService;

    /**
     * save studentFeeDescription
     *
     * @return
     * @throws WitcurveException
     */

    @PostMapping("/student-fee-description")
    @Timed
    public ResponseEntity<StudentFeeDescriptionDTO> saveStudentFeeDescription(@Valid @RequestBody StudentFeeDescriptionDTO studentFeeDescriptionDTO) throws WitcurveException {
        log.debug("Request to create studentFeeDescription : {}", studentFeeDescriptionDTO);
        if(studentFeeDescriptionDTO.getId() != null){
            throw new WitcurveException("New record can not have id already");
        }
        StudentFeeDescriptionDTO result = studentFeeDescriptionService.saveOrUpdate(studentFeeDescriptionDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * updates studentFeeDescription
     *
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/student-fee-description")
    @Timed
    public ResponseEntity<StudentFeeDescriptionDTO> updateStudentFeeDescription(@Valid @RequestBody StudentFeeDescriptionDTO studentFeeDescriptionDTO) throws WitcurveException {
        log.debug("Request to  update studentFeeDescription : {}", studentFeeDescriptionDTO);
        if(studentFeeDescriptionDTO.getId() == null){
            throw new WitcurveException("Id is require to update record ");
        }
        StudentFeeDescriptionDTO result = studentFeeDescriptionService.saveOrUpdate(studentFeeDescriptionDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
