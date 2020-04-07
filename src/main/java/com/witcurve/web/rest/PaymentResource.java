package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import com.witcurve.domain.enumeration.SubscriptionPackage;
import com.witcurve.domain.enumeration.TransactionMode;
import com.witcurve.service.PaymentService;
import com.witcurve.service.dto.PaymentOrderDTO;
import com.witcurve.service.dto.PaytmRequestDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(AnalyticsResource.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment/offline/create-order")
    @Timed
    public ResponseEntity<PaymentOrderDTO> createOfflineOrder(@RequestBody PaymentOrderDTO paymentOrderDTO) throws WitcurveException {
        log.debug("Rest API for creating offline order");
        if (paymentOrderDTO.getTransactionMode() != TransactionMode.CASH && paymentOrderDTO.getTransactionMode() != TransactionMode.IMPS) {
            throw new WitcurveException("Only CASH and IMPS transaction mode is allowed in offline orders");
        }

        return ResponseEntity.ok().body(paymentService.createPaymentOrder(paymentOrderDTO));
    }

    @PostMapping("/payment/paytm/create-order")
    @Timed
    public ResponseEntity<PaytmRequestDTO> createPaytmOrder(@RequestParam Long studentId,
                                                            @RequestParam(required = false) String mobileNumber,
                                                            @RequestParam SubscriptionPackage subscriptionPackage) throws WitcurveException {
        log.debug("Rest API for creating paytm order");
        return ResponseEntity.ok().body(paymentService.createPaytmOrder(studentId, mobileNumber, subscriptionPackage));
    }

    //TODO - temp service remove it or modify it after final changes
    @PostMapping("/payment/paytm/test-order")
    @Timed
    public ResponseEntity<PaytmRequestDTO> createPaytmOrder() throws WitcurveException {
        log.debug("Rest API for test paytm order");
        PaytmRequestDTO paytmRequestDTO = new PaytmRequestDTO();
        paytmRequestDTO.setMerchantMid("WITCUR13574383497047");
        paytmRequestDTO.setOrderId(String.valueOf(System.currentTimeMillis()));
        paytmRequestDTO.setChannelId("WEB");
        paytmRequestDTO.setCustomerId("test");
        paytmRequestDTO.setMobileNo("9502036596");
        paytmRequestDTO.setTransactionAmount("1.0");
        paytmRequestDTO.setWebsite("APPPROD");
        paytmRequestDTO.setIndustryTypeId("PrivateEducation");
        paytmRequestDTO.setCallbackUrl("https://www.witcurve.com/submission.html?orderId="+paytmRequestDTO.getOrderId());

        Set<ConstraintViolation<PaytmRequestDTO>> violations = (Validation.buildDefaultValidatorFactory())
            .getValidator().validate(paytmRequestDTO);
        if (!CollectionUtils.isEmpty(violations)) {
            violations.forEach(violation -> log.error(violation.getMessage()));
            throw new WitcurveException("Validation for Paytm Request Order DTO Failed");
        }

        TreeMap paytmParams = (new ObjectMapper()).convertValue(paytmRequestDTO, TreeMap.class);
        String checkSumHash = null;
        try {
            checkSumHash = CheckSumServiceHelper.getCheckSumServiceHelper()
                .genrateCheckSum("3WuVx2gN2#0ixC#!", paytmParams);
        } catch (Exception e) {
            throw new WitcurveException("error", e);
        }

        paytmRequestDTO.setCheckSumHash(checkSumHash);

        return ResponseEntity.ok().body(paytmRequestDTO);
    }

    @PostMapping("/payment/paytm/verify-order-status")
    @Timed
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> verifyOrderStatus(@RequestParam String orderId,
                                                     @RequestParam(required = false, defaultValue = "false") Boolean updateSubscription) throws WitcurveException {
        log.debug("Rest API for verification order status {}", orderId);
        return ResponseEntity.ok().body(paymentService.isTransactionComplete(orderId, updateSubscription));
    }

    @PatchMapping("/payment/update-transaction-id")
    @Timed
    public ResponseEntity<PaymentOrderDTO> updateTransactionId(@RequestParam String orderId,
                                                               @RequestParam String transactionId) throws WitcurveException {
        log.debug("Rest API for updating transaction id of payment order");
        return ResponseEntity.ok().body(paymentService.updateTransactionId(orderId, transactionId));

    }

    @PatchMapping("/payment/update-payout")
    @Timed
    public ResponseEntity<PaymentOrderDTO> updatePayout(@RequestParam String orderId) throws WitcurveException {
        log.debug("Rest API for updating payout status of payment order");
        return ResponseEntity.ok().body(paymentService.updatePayout(orderId));
    }


    @GetMapping("/payment/offline/pending-payout")
    @Timed
    public ResponseEntity<List<PaymentOrderDTO>> getOfflineOrdersWithoutPayouts() throws WitcurveException {
        log.debug("Rest API for getting offline orders which have pending payout");
        return ResponseEntity.ok().body(paymentService.getOfflineOrdersWithoutPayouts());
    }
}
