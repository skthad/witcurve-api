package com.witcurve.repository;

import com.witcurve.domain.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    PaymentOrder findByStudentIdAndId(Long studentId, Long orderId);
}
