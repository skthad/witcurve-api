package com.witcurve.repository;

import com.witcurve.domain.PaymentOrder;
import com.witcurve.domain.enumeration.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    PaymentOrder findByOrderId(String orderId);

    List<PaymentOrder> findByTransactionStatus(TransactionStatus transactionStatus);

    @Query("select o from PaymentOrder o where o.paymentGateway = 'OFFLINE' and o.payout = false")
    List<PaymentOrder> findOfflineOrdersWithoutPayout();
}
