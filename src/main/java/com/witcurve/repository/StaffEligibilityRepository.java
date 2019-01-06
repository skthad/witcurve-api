package com.witcurve.repository;

import com.witcurve.domain.MasterSubject;
import com.witcurve.domain.StaffEligibility;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffEligibilityRepository extends JpaRepository<StaffEligibility, Long> {

    @Query("Select distinct se.staff.id from StaffEligibility se " +
        "where se.standard.grade = ?1 and se.masterSubject = ?2 and " +
        "se.staff.schoolInfo.school.id = ?3 and se.staff.id != ?4")
    List<Long> findStaffByGradeAndSubject(Grade grade, MasterSubject masterSubject, Long schoolId, Long staffId);

    @Query("Select distinct se.staff.id from StaffEligibility se " +
        "where se.standard.id = ?1 and se.staff.schoolInfo.school.id = ?2 and se.staff.id != ?3")
    List<Long> findStaffByStandard(Long standardId, Long schoolId, Long staffId);

    @Query("Select distinct se.staff.id from StaffEligibility se  " +
        "where se.standard.grade = ?1 and se.staff.schoolInfo.school.id = ?2 and se.staff.id != ?3")
    List<Long> findStaffByGrade(Grade grade, Long schoolId, Long staffId);

}
