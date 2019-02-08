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

    @Query("Select la from LeaveApplication la where la.appliedStudent.id = ?1 and la.fromLeaveDate between ?2 and ?3 order by la.fromLeaveDate")
    List<LeaveApplication> findByStudentId(Long appliedStudentId, LocalDate startDate, LocalDate endDate);

    @Query("Select la from LeaveApplication la where la.appliedStudent.id =?1 and la.fromLeaveDate between ?2 and ?3 and la.status = ?4 order by la.fromLeaveDate")
    List<LeaveApplication> findByStudentAndStatus(Long appliedStudentId, LocalDate startDate, LocalDate endDate, ApprovalStatus status);

    @Query("Select la from LeaveApplication la where la.appliedStudent.id in ?1 and la.fromLeaveDate between ?2 and ?3 order by la.appliedStudent.id, la.fromLeaveDate")
    List<LeaveApplication> findByStudentList(List<Long> studentIds, LocalDate startDate, LocalDate endDate);

    @Query("Select la from LeaveApplication la where la.appliedStudent.id in ?1 and la.fromLeaveDate between ?2 and ?3 and la.status = ?4 order by la.appliedStudent.id, la.fromLeaveDate")
    List<LeaveApplication> findByStudentListAndStatus(List<Long> studentIds, LocalDate startDate, LocalDate endDate, ApprovalStatus status);

    @Query("Select la from LeaveApplication la where la.appliedStaff.id = ?1 and la.fromLeaveDate between ?2 and ?3 order by la.fromLeaveDate")
    List<LeaveApplication> findByStaffId(Long appliedStaffId, LocalDate startDate, LocalDate endDate);

    @Query("Select la from LeaveApplication la where la.appliedStaff.id =?1 and la.fromLeaveDate between ?2 and ?3 and la.status = ?4 order by la.fromLeaveDate")
    List<LeaveApplication> findByStaffIdAndStatus(Long appliedStaffId, LocalDate startDate, LocalDate endDate, ApprovalStatus status);

    @Query("Select la from LeaveApplication la where la.appliedStaff.schoolInfo.id = ?1 and la.fromLeaveDate between ?2 and ?3 order by la.appliedStaff.id, la.fromLeaveDate")
    List<LeaveApplication> findByStaffInSchoolInfoId(Long schoolInfoId, LocalDate fromDate, LocalDate toDate);

    @Query("Select la from LeaveApplication la where la.appliedStaff.schoolInfo.id = ?1 and la.fromLeaveDate between ?2 and ?3 and la.status = ?4 order by la.appliedStaff.id, la.fromLeaveDate")
    List<LeaveApplication> findByStaffInSchoolInfoIdAndStatus(Long schoolInfoId, LocalDate fromDate, LocalDate toDate, ApprovalStatus status);

}
