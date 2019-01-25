package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.PayrollCycleService;
import com.witcurve.service.dto.PayrollCycleDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PayrollCycleResource {

    private final Logger log = LoggerFactory.getLogger(PayrollCycleResource.class);

    @Autowired
    PayrollCycleService payrollCycleService;

    @PostMapping("/payroll-cycle")
    @Timed
    public ResponseEntity<List<PayrollCycleDTO>> saveOrUpdate(@RequestBody @Valid List<PayrollCycleDTO> payrollCycleDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request to save PayrollCycles");
        List<PayrollCycleDTO> result = payrollCycleService.saveOrUpdate(payrollCycleDTOs);
        return ResponseEntity.created(new URI("/api/payroll-cycle/"))
            .headers(HeaderUtil.createEntityCreationAlert("payroll-cycle", "payrollCycles"))
            .body(result);
    }

    /**
     * get payroll cycles by schoolInfo
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/payroll-cycle/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<PayrollCycleDTO>> getPayrollCyclesBySchoolInfoId(@PathVariable("schoolInfoId") Long schoolInfoId,
                                                                                @RequestParam(value = "year", required = false) Integer year,
                                                                                @RequestParam(value = "month", required = false) Month month) throws WitcurveException {
        log.debug("Request to get payroll details for schoolInfoId {}", schoolInfoId);
        List<PayrollCycleDTO> result = payrollCycleService.getPayrollCyclesForSchoolInfo(schoolInfoId, year, month);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
