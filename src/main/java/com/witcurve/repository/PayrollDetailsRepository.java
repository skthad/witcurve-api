package com.witcurve.repository;

import com.witcurve.domain.PayrollDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Repository
public interface PayrollDetailsRepository extends JpaRepository<PayrollDetails, Long> {

    @Query("select pd from PayrollDetails pd where pd.staff.id = ?1 " +
        "and (pd.deactivationDate is null or pd.deactivationDate > now()) " +
        "order by deactivationDate nulls first ")
    List<PayrollDetails> findActivePayrollDetailsForActiveStaff(Long staffId);

    @Query("select pd from PayrollDetails pd where pd.staff.id = ?1 " +
        "order by deactivationDate nulls last ")
    List<PayrollDetails> findAllPayrollDetailsForStaff(Long staffId);

    @Query("select pd from PayrollDetails pd where pd.staff.schoolInfo.id = ?1 " +
        "and (pd.deactivationDate is null or pd.deactivationDate > now()) " +
        "and pd.staff.user.activated = true and pd.staff.id not in (" +
        "select p.payrollDetails.staff.id " +
        "from Payroll p where p.payrollCycle.schoolInfo.id = ?1 " +
        "and p.payrollCycle.year = ?2 and p.payrollCycle.month = ?3) " +
        "order by deactivationDate nulls first ")
    List<PayrollDetails> findPayrollDetailsForActiveStaffInSchoolInfo(Long schoolInfoId, Integer year, Month month);
}
