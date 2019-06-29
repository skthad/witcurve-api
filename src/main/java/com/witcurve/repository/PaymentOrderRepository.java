package com.witcurve.repository;

import com.witcurve.domain.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    PaymentOrder findByOrderId(String orderId);
    PaymentOrder findByStudentIdAndOrderId(Long studentId, Long orderId);
}
