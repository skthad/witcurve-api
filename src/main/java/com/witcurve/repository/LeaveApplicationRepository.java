package com.witcurve.repository;

import com.witcurve.domain.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

    List<LeaveApplication> findBySessionIdAndAppliedStudentIdOrderByCreatedDate(Long sessionId, Long appliedStudentId);

    List<LeaveApplication> findBySessionIdAndAppliedStudentIdAndApprovedTrueOrderByCreatedDate(Long sessionId, Long appliedStudentId);

    List<LeaveApplication> findBySessionIdAndAppliedStudentIdAndApprovedFalseOrderByCreatedDate(Long sessionId, Long appliedStudentId);

    @Query("Select la from LeaveApplication la where la.session.id =?1 and la.appliedStudent.id in ?2 order by la.fromLeaveDate")
    List<LeaveApplication> findLeaveAppicationsForStudentsInASession(Long sessionId,List<Long> studentIds);

    @Query("Select la from LeaveApplication la where la.session.id =?1 and la.appliedStudent.id in ?2 order by la.fromLeaveDate")
    List<LeaveApplication> findLeaveAppicationsForStudentInASession(Long sessionId,Long studentId);

    @Query("Select la from LeaveApplication la where la.session.id =?1 and la.appliedStaff.id in ?2 order by la.fromLeaveDate")
    List<LeaveApplication> findLeaveAppicationsForStaffInASession(Long sessionId,Long staffId);

    @Query("Select la from LeaveApplication la where la.session.id =?1 and la.approved = true and la.appliedStudent.id in ?2 order by la.fromLeaveDate")
    List<LeaveApplication> findApprovedLeaveApplicationsForStudentsInASession(Long sessionId, List<Long> studentIds);

    @Query("Select la from LeaveApplication la where la.session.id =?1 and la.approved = false and la.appliedStudent.id in ?2 order by la.fromLeaveDate")
    List<LeaveApplication> findUnApprovedLeaveApplicationsForStudentsInASession(Long sessionId, List<Long> studentIds);

    List<LeaveApplication> findBySessionIdAndAppliedStaffIdOrderByFromLeaveDate(Long sessionId,Long appliedStaffId);

    List<LeaveApplication> findBySessionIdAndAppliedStaffIdAndApprovedTrueOrderByFromLeaveDate(Long sessionId,Long appliedStaffId);

    List<LeaveApplication> findBySessionIdAndAppliedStaffIdAndApprovedFalseOrderByFromLeaveDate(Long sessionId,Long appliedStaffId);

    List<LeaveApplication> findByAppliedStudentIdAndFromLeaveDate(Long appliedStudentId, LocalDate fromLeaveDate);

    List<LeaveApplication> findByAppliedStaffIdAndFromLeaveDate(Long appliedStaffId, LocalDate fromLeaveDate);

    @Query("Select la from LeaveApplication la where la.session.id =?1 and la.appliedStudent.id = ?2 and (la.fromLeaveDate between ?3 and ?4 or la.toLeaveDate between ?3 and ?4) order by la.fromLeaveDate")
    List<LeaveApplication> findLeaveApplicationsForStudentInADateRange(Long sessionId, Long studentId, LocalDate fromDate, LocalDate toDate);

    @Query("Select la from LeaveApplication la where la.session.id =?1 and la.appliedStaff.id = ?2 and (la.fromLeaveDate between ?3 and ?4 or la.toLeaveDate between ?3 and ?4) order by la.fromLeaveDate")
    List<LeaveApplication> findLeaveApplicationsForStaffInADateRange(Long sessionId, Long staffId, LocalDate fromDate, LocalDate toDate);

    @Query("Select la from LeaveApplication la where la.appliedStudent.id = ?1 and (la.fromLeaveDate <= ?2) and (la.toLeaveDate >= ?2)")
    LeaveApplication findLeaveForStudentOnDate(Long studentId, LocalDate date);

}
