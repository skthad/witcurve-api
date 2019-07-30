package com.witcurve.service;

import com.witcurve.domain.enumeration.SubscriptionPackage;
import com.witcurve.service.dto.PaytmRequestDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;

public interface PaymentService {

    PaytmRequestDTO createPaytmOrder(Long studentId, String mobileNumber, SubscriptionPackage subscriptionPackage) throws WitcurveException;

    Boolean isTransactionComplete(String orderId, Boolean updateSubscription) throws WitcurveException;

    void processPendingTransactions();
}
