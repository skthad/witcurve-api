package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.PaytmErrorCodes;
import com.witcurve.service.PaytmCallBackService;
import com.witcurve.web.rest.vm.PaytmStatusCheckVM;
import com.witcurve.web.rest.vm.PaytmVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaytmCallBackResource {
    private final Logger log = LoggerFactory.getLogger(PaytmCallBackResource.class);

    @Autowired
    PaytmCallBackService paytmCallBackService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/paytm-fee/validation")
    @Timed
    public ResponseEntity<PaytmVM> getStudentFee(@RequestParam(required = false) String instituteName, @RequestParam(required = false) String admissionId, @RequestParam(required = false) String type) {
        log.debug("Request to get fee detail of given institute with admissionId and type : ", instituteName, admissionId, type);
        PaytmVM paytmVM;
        try {
            paytmVM = paytmCallBackService.getStudentFee(instituteName, admissionId, type, request);
        } catch (Exception e) {
            paytmVM = new PaytmVM();
            paytmVM.setErrorCode(PaytmErrorCodes.INTERNAL_SERVER_ERROR.getValue());
        }
        return ResponseEntity.ok(paytmVM);
    }


    @PostMapping("/paytm-fee/post-payment")
    @Timed
    public ResponseEntity<Map<String, String>> statusCheck(@RequestBody @Valid PaytmStatusCheckVM
                                                               paytmStatusCheckVM, @RequestParam(required = false) String orderId, @RequestParam(required = false) String
                                                               admissionId, @RequestParam(required = false) String instituteName) {
        log.debug("Request to check status with given  order Id, enrollment no, instituteName : ", orderId, admissionId, instituteName);
        Map<String, String> responseMap = null;
        try {
            responseMap = paytmCallBackService.save(paytmStatusCheckVM, orderId, admissionId, instituteName, request);
        } catch (Exception e) {
            responseMap.put("errorcode", String.valueOf(PaytmErrorCodes.INTERNAL_SERVER_ERROR.getValue()));
        }
        return ResponseEntity.ok(responseMap);
    }
}
