package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.PaymentOrder;
import com.witcurve.domain.enumeration.PaymentGateway;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(AnalyticsResource.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment/self/create-order")
    @Timed
    public ResponseEntity<PaymentOrderDTO> createSelfOrder(@RequestParam Long studentId,
                                                           @RequestParam TransactionMode transactionMode,
                                                           @RequestParam SubscriptionPackage subscriptionPackage) throws WitcurveException {
        return ResponseEntity.ok().body(paymentService.createPaymentOrder(studentId, subscriptionPackage, PaymentGateway.SELF, transactionMode));
    }

    @PostMapping("/payment/paytm/create-order")
    @Timed
    public ResponseEntity<PaytmRequestDTO> createPaytmOrder(@RequestParam Long studentId,
                                                            @RequestParam(required = false) String mobileNumber,
                                                            @RequestParam SubscriptionPackage subscriptionPackage) throws WitcurveException {
        return ResponseEntity.ok().body(paymentService.createPaytmOrder(studentId, mobileNumber, subscriptionPackage));
    }

    @PostMapping("/payment/paytm/verify-order-status")
    @Timed
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> verifyOrderStatus(@RequestParam String orderId,
                                                     @RequestParam(required = false, defaultValue = "false") Boolean updateSubscription) throws WitcurveException {
        log.debug("Rest API for verification order status {}", orderId);
        return ResponseEntity.ok().body(paymentService.isTransactionComplete(orderId, updateSubscription));
    }
}
