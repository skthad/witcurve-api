package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.PayrollService;
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
import java.util.List;

@RestController
@RequestMapping("/api")
public class PayrollDetailsResource {

    private final Logger log = LoggerFactory.getLogger(PayrollDetailsResource.class);

    @Autowired
    PayrollService payrollService;

    @PostMapping("/payroll-details")
    @Timed
    public ResponseEntity<PayrollDetailsDTO> createPayrollDetails(@RequestBody @Valid PayrollDetailsDTO payrollDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to save or update PayrollDetails");
        try {
            PayrollDetailsDTO result = payrollService.saveOrUpdate(payrollDetailsDTO);
            return ResponseEntity.created(new URI("/api/payroll-details/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("payroll-details", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
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

    @GetMapping("/payroll-details/staff/{staffId}")
    @Timed
    public ResponseEntity<List<PayrollDetailsDTO>> getPayrollDetailsByStaffId(@PathVariable("staffId") Long staffId,
                                                                              @RequestParam(value = "activeOnly", required = false, defaultValue = "true") Boolean activeOnly) throws WitcurveException {
        log.debug("Request to get payroll details for staffId {}", staffId);
        List<PayrollDetailsDTO> result = payrollService.getPayrollDetailsForStaff(staffId, activeOnly);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
