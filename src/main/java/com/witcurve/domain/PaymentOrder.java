package com.witcurve.domain;

import com.witcurve.domain.enumeration.PaymentGateway;
import com.witcurve.domain.enumeration.SubscriptionPackage;
import com.witcurve.domain.enumeration.TransactionMode;
import com.witcurve.domain.enumeration.TransactionStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="payment_order", uniqueConstraints = {
    @UniqueConstraint(name = "order_id_UK",
    columnNames = {"order_id"})
})
public class PaymentOrder extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private String orderId = String.valueOf(System.currentTimeMillis());

    @NotNull
    @ManyToOne
    private Student student;

    @NotNull
    @Column(name = "subscription_package", nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionPackage subscriptionPackage;

    @NotNull
    @Column(name = "payment_gateway", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;

    @NotNull
    @Column(name = "transaction_amount", nullable = false)
    private Double transactionAmount;

    @NotNull
    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "transaction_charge")
    private Double transactionCharge;

    @Column(name = "transaction_mode")
    @Enumerated(EnumType.STRING)
    private TransactionMode transactionMode;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "response_code", length = 10)
    private String responseCode;

    @Column(name = "response_message", length = 500)
    private String responseMessage;

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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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
}
