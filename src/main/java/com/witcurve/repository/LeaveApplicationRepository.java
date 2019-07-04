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

    @Query("Select la from LeaveApplication la where la.appliedStudent.id =?1 and (la.fromLeaveDate between ?2 and ?3 or ?2 between la.fromLeaveDate and la.toLeaveDate) and la.status in ?4 order by la.fromLeaveDate")
    List<LeaveApplication> findByStudentAndStatuses(Long appliedStudentId, LocalDate startDate, LocalDate endDate, List<ApprovalStatus> statuses);

    @Query("Select la from LeaveApplication la where la.appliedStudent.id in ?1 and (la.fromLeaveDate between ?2 and ?3 or ?2 between la.fromLeaveDate and la.toLeaveDate) and la.status in ?4 order by la.fromLeaveDate")
    List<LeaveApplication> findByStudentListAndStatuses(List<Long> studentIds, LocalDate startDate, LocalDate endDate, List<ApprovalStatus> statuses);

    @Query("Select la from LeaveApplication la where la.appliedStaff.id =?1 and (la.fromLeaveDate between ?2 and ?3 or ?2 between la.fromLeaveDate and la.toLeaveDate) and la.status in ?4 order by la.fromLeaveDate")
    List<LeaveApplication> findByStaffIdAndStatus(Long appliedStaffId, LocalDate startDate, LocalDate endDate, List<ApprovalStatus> statuses);

    @Query("Select la from LeaveApplication la where la.appliedStaff.schoolInfo.id = ?1 and (la.fromLeaveDate between ?2 and ?3 or ?2 between la.fromLeaveDate and la.toLeaveDate) and la.status in ?4 order by la.fromLeaveDate")
    List<LeaveApplication> findByStaffInSchoolInfoIdAndStatus(Long schoolInfoId, LocalDate fromDate, LocalDate toDate, List<ApprovalStatus> statuses);

}
