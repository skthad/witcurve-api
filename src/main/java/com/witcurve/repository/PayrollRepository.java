package com.witcurve.repository;

import com.witcurve.domain.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    @Query("select p from Payroll p where p.staffId = ?1 and p.year = ?2")
    List<Payroll> findPayrollsForStaffInYear(Long staffId, Integer year);

    @Query("select p from Payroll p where p.staffId = ?1 and p.year = ?2 and p.month = ?3")
    List<Payroll> findActivePayrollForStaffInMonth(Long staffId, Integer year, Month month);

    @Query("select p from Payroll p where p.staffId = ?1 and p.sessionId = ?2")
    List<Payroll> findPayrollsForStaffInAcademicSession(Long staffId, Long sessionId);

}
