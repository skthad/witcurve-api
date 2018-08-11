package com.witcurve.repository;

import com.witcurve.domain.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidaysRepository extends JpaRepository<Holiday, Long> {
}

