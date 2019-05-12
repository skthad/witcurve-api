package com.witcurve.domain;

import com.witcurve.domain.enumeration.PaymentGateway;
import com.witcurve.domain.enumeration.SubscriptionPackage;
import com.witcurve.domain.enumeration.TransactionMode;
import com.witcurve.domain.enumeration.TransactionStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="payment_order")
public class PaymentOrder extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Student student;

    @NotNull
    @Column(name = "subscription_package")
    @Enumerated(EnumType.STRING)
    private SubscriptionPackage subscriptionPackage;

    @NotNull
    @Column(name = "payment_gateway")
    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;

    @NotNull
    @Column(name = "transaction_amount")
    private Long transactionAmount;

    @NotNull
    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "transaction_charge")
    private Long transactionCharge;

    @Column(name = "transaction_mode")
    private TransactionMode transactionMode;

    @Column(name = "transaction_id")
    private String transactionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Long transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Long getTransactionCharge() {
        return transactionCharge;
    }

    public void setTransactionCharge(Long transactionCharge) {
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
}
