package com.witcurve.repository;

import com.witcurve.domain.StandardReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardReportRepository extends JpaRepository<StandardReport, Long> {

}
