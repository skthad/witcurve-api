package com.witcurve.repository;

import com.witcurve.domain.LeaveApplication;
import com.witcurve.domain.enumeration.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {

    @Query("Select la from LeaveApplication la where la.schoolInfo.id =?1 and la.appliedStudent.id = ?2 and la.fromLeaveDate > ?3 order by la.fromLeaveDate")
    List<LeaveApplication> findBySchoolInfoIdAndAppliedStudentId(Long schoolInfoId, Long appliedStudentId, LocalDate startDate);

    @Query("Select la from LeaveApplication la where la.schoolInfo.id =?1 and la.appliedStudent.id = ?2 and la.status = ?3 and la.fromLeaveDate > ?4 order by la.fromLeaveDate")
    List<LeaveApplication> findBySchoolInfoIdAndAppliedStudentIdWithStatus(Long schoolInfoId, Long appliedStudentId, ApprovalStatus status, LocalDate startDate);

    @Query("Select la from LeaveApplication la where la.schoolInfo.id =?1 and la.appliedStaff.id = ?2 and la.fromLeaveDate > ?3 order by la.fromLeaveDate")
    List<LeaveApplication> findBySchoolInfoIdAndAppliedStaffId(Long schoolInfoId, Long appliedStaffId, LocalDate startDate);

    @Query("Select la from LeaveApplication la where la.schoolInfo.id =?1 and la.appliedStaff.id = ?2 and la.status = ?3 and la.fromLeaveDate > ?4 order by la.fromLeaveDate")
    List<LeaveApplication> findBySchoolInfoIdAndAppliedStaffIdWithStatus(Long schoolInfoId, Long appliedStaffId, ApprovalStatus status, LocalDate startDate);

    @Query("Select la from LeaveApplication la where la.schoolInfo.id =?1 and la.appliedStudent.id in ?2 and la.fromLeaveDate > ?3 order by la.fromLeaveDate")
    List<LeaveApplication> findLeaveAppicationsForStudentsForSchoolInfoId(Long schoolInfoId,List<Long> studentIds, LocalDate startDate);

    @Query("Select la from LeaveApplication la where la.schoolInfo.id =?1 and la.appliedStudent.id in ?2 and la.status = ?3 and la.fromLeaveDate > ?4 order by la.fromLeaveDate")
    List<LeaveApplication> findLeaveAppicationsForStudentsForSchoolInfoIdWithStatus(Long schoolInfoId,List<Long> studentIds, ApprovalStatus status, LocalDate startDate);

    @Query("Select la from LeaveApplication la where la.schoolInfo.id =?1 and la.appliedStudent.id = ?2 and (la.fromLeaveDate between ?3 and ?4 or la.toLeaveDate between ?3 and ?4) order by la.fromLeaveDate")
    List<LeaveApplication> findLeaveApplicationsForStudentInADateRange(Long schoolInfoId, Long studentId, LocalDate fromDate, LocalDate toDate);

    @Query("Select la from LeaveApplication la where la.schoolInfo.id =?1 and la.appliedStaff.id = ?2 and (la.fromLeaveDate between ?3 and ?4 or la.toLeaveDate between ?3 and ?4) order by la.fromLeaveDate")
    List<LeaveApplication> findLeaveApplicationsForStaffInADateRange(Long schoolInfoId, Long staffId, LocalDate fromDate, LocalDate toDate);

    @Query("Select la from LeaveApplication la where la.appliedStudent.id = ?1 and (la.fromLeaveDate <= ?2) and (la.toLeaveDate >= ?2)")
    LeaveApplication findLeaveForStudentOnDate(Long studentId, LocalDate date);

    @Query("Select la from LeaveApplication la where la.appliedStaff.id = ?1 and (la.fromLeaveDate <= ?2) and (la.toLeaveDate >= ?2)")
    LeaveApplication findLeaveForStaffOnDate(Long staffId, LocalDate date);

    @Query("Select la from LeaveApplication la where la.schoolInfo.id = ?1 and la.appliedStaff.id is not null and la.appliedStudent.id is null and la.fromLeaveDate >= ?2 and la.status=?3 order by la.fromLeaveDate")
    List<LeaveApplication> findLeaveApplicationsForAllStaffsInSchool(Long schoolInfoId,LocalDate startDate,ApprovalStatus status);

}
