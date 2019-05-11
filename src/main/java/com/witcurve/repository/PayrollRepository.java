package com.witcurve.repository;

import com.witcurve.domain.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    @Query("select p from Payroll p where p.payrollDetails.staff.id = ?1 " +
        "and p.payrollCycle.year = ?2 and p.payrollCycle.month = ?3")
    List<Payroll> findPayrollForStaffInMonth(Long staffId, Integer year, Month month);

    @Query("select p from Payroll p where p.payrollDetails.staff.id = ?1 " +
        "and p.payrollCycle.year = ?2")
    List<Payroll> findPayrollsForStaffInYear(Long staffId, Integer year);

    @Query("select p from Payroll p where p.payrollDetails.staff.id = ?1")
    List<Payroll> findAllPayrollsForStaff(Long staffId);

    @Query("select p from Payroll p where p.payrollCycle.schoolInfo.id = ?1 " +
        "and p.payrollCycle.year = ?2 and p.payrollCycle.month = ?3")
    List<Payroll> findPayrollForScoolInfoInMonth(Long schoolInfoId, Integer year, Month month);

    @Query("select p from Payroll p where p.payrollCycle.schoolInfo.id = ?1 " +
        "and p.payrollCycle.year = ?2")
    List<Payroll> findPayrollsForScoolInfoInYear(Long schoolInfoId, Integer year);

    @Query("select p from Payroll p where p.payrollCycle.schoolInfo.id = ?1")
    List<Payroll> findAllPayrollsForScoolInfo(Long schoolInfoId);

}
