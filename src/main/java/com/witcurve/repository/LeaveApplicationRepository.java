package com.witcurve.repository;

import com.witcurve.domain.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

    List<LeaveApplication> findBySessionIdAndAppliedStudentIdOrderByFromLeaveDate(Long sessionId,Long appliedStudentId);

    List<LeaveApplication> findBySessionIdAndAppliedStudentIdAndApprovedTrueOrderByFromLeaveDate(Long sessionId,Long appliedStudentId);

    List<LeaveApplication> findBySessionIdAndAppliedStudentIdAndApprovedFalseOrderByFromLeaveDate(Long sessionId,Long appliedStudentId);

    @Query("Select la from LeaveApplication la where la.session.id =?1 and la.appliedStudent.id in ?2 order by la.leaveDate")
    List<LeaveApplication> findLeaveAppicationsForStudentsInASession(Long sessionId,List<Long> studentIds);

    @Query("Select la from LeaveApplication la where la.session.id =?1 and la.approved = true and la.appliedStudent.id in ?2 order by la.leaveDate")
    List<LeaveApplication> findApprovedLeaveAppicationsForStudentsInASession(Long sessionId,List<Long> studentIds);

    @Query("Select la from LeaveApplication la where la.session.id =?1 and la.approved = false and la.appliedStudent.id in ?2 order by la.leaveDate")
    List<LeaveApplication> findUnApprovedLeaveAppicationsForStudentsInASession(Long sessionId,List<Long> studentIds);

    List<LeaveApplication> findBySessionIdAndAppliedStaffIdOrderByFromLeaveDate(Long sessionId,Long appliedStaffId);

    List<LeaveApplication> findBySessionIdAndAppliedStaffIdAndApprovedTrueOrderByFromLeaveDate(Long sessionId,Long appliedStaffId);

    List<LeaveApplication> findBySessionIdAndAppliedStaffIdAndApprovedFalseOrderByFromLeaveDate(Long sessionId,Long appliedStaffId);

    List<LeaveApplication> findByAppliedStudentIdAndFromLeaveDate(Long appliedStudentId, LocalDate fromLeaveDate);

    List<LeaveApplication> findByAppliedStaffIdAndFromLeaveDate(Long appliedStaffId, LocalDate fromLeaveDate);
}
