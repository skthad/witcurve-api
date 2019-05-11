package com.witcurve.repository;

import com.witcurve.domain.PayrollDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollDetailsRepository extends JpaRepository<PayrollDetails, Long> {

    @Query("select pd from PayrollDetails pd where pd.staff.id = ?1 " +
        "and (pd.deactivationDate is null or pd.deactivationDate > now()) " +
        "order by deactivationDate nulls first ")
    List<PayrollDetails> findActivePayrollDetailsForStaff(Long staffId);

    @Query("select pd from PayrollDetails pd where pd.staff.id = ?1 " +
        "order by deactivationDate nulls last ")
    List<PayrollDetails> findAllPayrollDetailsForStaff(Long staffId);
}
