package com.witcurve.repository;

import com.witcurve.domain.PaymentOrder;
import com.witcurve.domain.enumeration.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    PaymentOrder findByOrderId(String orderId);

    List<PaymentOrder> findByTransactionStatus(TransactionStatus transactionStatus);
}
