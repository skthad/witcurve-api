package com.witcurve.repository;

import com.witcurve.domain.SessionFeeDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionFeeDescriptionRepository extends JpaRepository<SessionFeeDescription, Long> {
}
