package com.witcurve.repository;

import com.witcurve.domain.PayrollCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

@Repository
public interface PayrollCycleRepository extends JpaRepository<PayrollCycle, Long> {

    @Query("select pc from PayrollCycle pc where pc.schoolInfo.id = ?1 and pc.year = ?2 and pc.month = ?3")
    List<PayrollCycle> findBySchoolInfoIdAndMonth(Long schoolInfoId, Integer year, Month month);

    @Query("select pc from PayrollCycle pc where pc.schoolInfo.id = ?1 and pc.year = ?2")
    List<PayrollCycle> findBySchoolInfoIdAndYear(Long schoolInfoId, Integer year);

    @Query("select pc from PayrollCycle pc where pc.schoolInfo.id = ?1")
    List<PayrollCycle> findBySchoolInfoId(Long schoolInfoId);

}
