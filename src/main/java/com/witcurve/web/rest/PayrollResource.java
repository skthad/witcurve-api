package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.PayrollService;
import com.witcurve.service.dto.PayrollDTO;
import com.witcurve.service.dto.PayrollDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PayrollResource {

    private final Logger log = LoggerFactory.getLogger(PayrollResource.class);

    @Autowired
    PayrollService payrollService;

    @PostMapping("/payroll-details")
    @Timed
    public ResponseEntity<PayrollDetailsDTO> createPayrollDetails(@RequestBody @Valid PayrollDetailsDTO payrollDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to save PayrollDetails");
        if (payrollDetailsDTO.getId() != null) {
            throw new WitcurveException("New payroll details can't already have an id");
        }
        try {
            PayrollDetailsDTO result = payrollService.saveOrUpdate(payrollDetailsDTO);
            return ResponseEntity.created(new URI("/api/payroll-details/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("payroll-details", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("staff_payroll_cycle_UK")) {
                throw new WitcurveException("Unique constraint (staff_id, payroll_cycle_id) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    @PutMapping("/payroll-details")
    @Timed
    public ResponseEntity<PayrollDetailsDTO> updatePayrollDetails(@RequestBody @Valid PayrollDetailsDTO payrollDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to update PayrollDetails");
        if (payrollDetailsDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        try {
            PayrollDetailsDTO result = payrollService.saveOrUpdate(payrollDetailsDTO);
            return ResponseEntity.created(new URI("/api/payroll-details/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("payroll-details", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("staff_payroll_cycle_UK")) {
                throw new WitcurveException("Unique constraint (staff_id, payroll_cycle_id) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get school by id
     * @param staffId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/payroll-details/{staffId}")
    @Timed
    public ResponseEntity<List<PayrollDetailsDTO>> getPayrollDetailsByStaffId(@PathVariable("staffId") Long staffId) throws WitcurveException {
        log.debug("Request to get payroll details for staffId {}", staffId);
        List<PayrollDetailsDTO> result = payrollService.getActivePayrollDetailsForStaff(staffId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/payrolls")
    @Timed
    public ResponseEntity<PayrollDTO> createPayroll(@RequestBody @Valid PayrollDTO payrollDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to save Payroll");
        if (payrollDTO.getId() != null) {
            throw new WitcurveException("New payroll can't already have an id");
        }
        try {
            PayrollDTO result = payrollService.saveOrUpdate(payrollDTO);
            return ResponseEntity.created(new URI("/api/payroll/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("payroll", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("staff_payroll_cycle_UK")) {
                throw new WitcurveException("Unique constraint (staff_id, payroll_cycle_id) in payrollDetails  violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    @PutMapping("/payrolls")
    @Timed
    public ResponseEntity<PayrollDTO> updatePayroll(@RequestBody @Valid PayrollDTO payrollDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to update Payroll");
        if (payrollDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        try {
            PayrollDTO result = payrollService.saveOrUpdate(payrollDTO);
            return ResponseEntity.created(new URI("/api/payroll/" + result.getId()))
                .headers(HeaderUtil.createEntityUpdateAlert("payroll", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("staff_payroll_cycle_UK")) {
                throw new WitcurveException("Unique constraint (school_info_id, month, year) in payrollDetails violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get payrolls by staff id
     * @param staffId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/payrolls/staff/{staffId}")
    @Timed
    public ResponseEntity<List<PayrollDTO>> getPayrollsByStaffId(@PathVariable("staffId") Long staffId,
                                                                       @RequestParam(value = "year", required = false) Integer year,
                                                                       @RequestParam(value = "month", required = false) Month month) throws WitcurveException {
        log.debug("Request to get payroll(s) for staffId {}", staffId);
        if (year == null && month != null) {
            throw new WitcurveException("year cannot be null");
        }
        List<PayrollDTO> result = payrollService.getPayrollsForStaff(staffId, year, month);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get payrolls by school info id
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/payrolls/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<PayrollDTO>> getPayrollsBySchoolInfoId(@PathVariable("schoolInfoId") Long schoolInfoId,
                                                                 @RequestParam(value = "year", required = false) Integer year,
                                                                 @RequestParam(value = "month", required = false) Month month) throws WitcurveException {
        log.debug("Request to get payroll(s) for schoolInfoId {}", schoolInfoId);
        if (year == null && month != null) {
            throw new WitcurveException("year cannot be null");
        }
        List<PayrollDTO> result = payrollService.getPayrollsForSchoolInfo(schoolInfoId, year, month);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
