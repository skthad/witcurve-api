package com.witcurve.service;

import com.witcurve.domain.enumeration.SubscriptionPackage;
import com.witcurve.service.dto.PaymentOrderDTO;
import com.witcurve.service.dto.PaytmRequestDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface PaymentService {

    PaytmRequestDTO createPaytmOrder(Long studentId, String mobileNumber, SubscriptionPackage subscriptionPackage) throws WitcurveException;

    PaymentOrderDTO createPaymentOrder(PaymentOrderDTO paymentOrderDTO) throws WitcurveException;

    Boolean isTransactionComplete(String orderId, Boolean updateSubscription) throws WitcurveException;

    void processPendingTransactions();

    PaymentOrderDTO updateTransactionId(String orderId, String transactionId) throws WitcurveException;

    PaymentOrderDTO updatePayout(String orderId) throws WitcurveException;

    List<PaymentOrderDTO> getOfflineOrdersWithoutPayouts() throws WitcurveException;
}
