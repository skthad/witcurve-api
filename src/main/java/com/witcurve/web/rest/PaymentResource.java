package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
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

import java.util.List;

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
