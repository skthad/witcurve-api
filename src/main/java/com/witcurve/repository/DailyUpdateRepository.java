package com.witcurve.repository;

import com.witcurve.domain.DailyUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyUpdateRepository  extends JpaRepository<DailyUpdate, Long> {

}
