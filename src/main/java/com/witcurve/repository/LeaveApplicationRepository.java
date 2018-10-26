package com.witcurve.repository;

import com.witcurve.domain.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

    List<LeaveApplication> findBySessionIdAndAppliedStudentId(Long sessionId,Long appliedStudentId);
    List<LeaveApplication> findBySessionIdAndAppliedStaffId(Long sessionId,Long appliedStaffId);

    }
