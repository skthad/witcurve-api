package com.witcurve.service;

import com.witcurve.domain.enumeration.PaymentGateway;
import com.witcurve.domain.enumeration.SubscriptionPackage;
import com.witcurve.domain.enumeration.TransactionMode;
import com.witcurve.service.dto.PaymentOrderDTO;
import com.witcurve.service.dto.PaytmRequestDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface PaymentService {



    PaytmRequestDTO createPaytmOrder(Long studentId, String mobileNumber, SubscriptionPackage subscriptionPackage) throws WitcurveException;

    PaymentOrderDTO createPaymentOrder(Long studentId, SubscriptionPackage subscriptionPackage, PaymentGateway paymentGateway,
                                       TransactionMode transactionMode) throws WitcurveException;

    Boolean isTransactionComplete(String orderId, Boolean updateSubscription) throws WitcurveException;

    void processPendingTransactions();
}
