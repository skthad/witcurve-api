package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.witcurve.domain.enumeration.PaymentGateway;
import com.witcurve.domain.enumeration.SubscriptionPackage;
import com.witcurve.domain.enumeration.TransactionMode;
import com.witcurve.domain.enumeration.TransactionStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentOrderDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderId;

    private Long studentId;

    private SubscriptionPackage subscriptionPackage;

    private PaymentGateway paymentGateway;

    private Double transactionAmount;

    private TransactionStatus transactionStatus;

    private Double transactionCharge;

    private TransactionMode transactionMode;

    private String transactionId;

    private String responseCode;

    private String responseMessage;

    private Boolean payout;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public SubscriptionPackage getSubscriptionPackage() {
        return subscriptionPackage;
    }

    public void setSubscriptionPackage(SubscriptionPackage subscriptionPackage) {
        this.subscriptionPackage = subscriptionPackage;
    }

    public PaymentGateway getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Double getTransactionCharge() {
        return transactionCharge;
    }

    public void setTransactionCharge(Double transactionCharge) {
        this.transactionCharge = transactionCharge;
    }

    public TransactionMode getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(TransactionMode transactionMode) {
        this.transactionMode = transactionMode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Boolean getPayout() {
        return payout;
    }

    public void setPayout(Boolean payout) {
        this.payout = payout;
    }
}
