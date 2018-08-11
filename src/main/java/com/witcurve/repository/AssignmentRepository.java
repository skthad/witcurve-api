package com.witcurve.repository;

import com.witcurve.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("select am from Assignment  am where am.postedDate = ?1 and am.timeTableUnit.id = ?2")
    List<Assignment> findAssignmentsByPostedDateAndTimeTableUnitId(LocalDate postedDate, Long timeTableUnitId);

    @Query("select am from Assignment  am where am.submissionDate = ?1 and am.timeTableUnit.id = ?2")
    List<Assignment> findAssignmentsBySubmissionDateAndTimeTableUnitId(LocalDate submissionDate, Long timeTableUnitId);
}
