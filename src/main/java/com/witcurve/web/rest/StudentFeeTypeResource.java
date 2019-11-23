package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StudentFeeTypeService;
import com.witcurve.service.dto.StudentFeeDescriptionDTO;
import com.witcurve.service.dto.StudentFeeTypeDTO;
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
public class StudentFeeTypeResource {

    private final Logger log = LoggerFactory.getLogger(StudentFeeTypeResource.class);

    @Autowired
    StudentFeeTypeService studentFeeTypeService;

    /**
     * save StudentFeeType
     *
     * @return
     * @throws WitcurveException
     */

    @PostMapping("/student-fee-type")
    @Timed
    public ResponseEntity<StudentFeeTypeDTO> saveStudentFeeType(@Valid @RequestBody StudentFeeTypeDTO studentFeeTypeDTO) throws WitcurveException {
        log.debug("Request to create StudentFeeType : {}", studentFeeTypeDTO);
        if(studentFeeTypeDTO.getId() != null){
            throw new WitcurveException("New record can not have id already");
        }
        StudentFeeTypeDTO result = studentFeeTypeService.saveOrUpdate(studentFeeTypeDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * updates StudentFeeType
     *
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/student-fee-type")
    @Timed
    public ResponseEntity<StudentFeeTypeDTO> updateStudentFeeType(@Valid @RequestBody StudentFeeTypeDTO studentFeeTypeDTO) throws WitcurveException {
        log.debug("Request to create StudentFeeType : {}", studentFeeTypeDTO);
        if(studentFeeTypeDTO.getId() == null){
            throw new WitcurveException("Id is require to update record");
        }
        StudentFeeTypeDTO result = studentFeeTypeService.saveOrUpdate(studentFeeTypeDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
