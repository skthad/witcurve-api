package com.witcurve.repository;

import com.witcurve.domain.MasterSubject;
import com.witcurve.domain.StaffEligibility;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffEligibilityRepository extends JpaRepository<StaffEligibility, Long> {

    @Query("Select se from StaffEligibility se where se.staff.schoolInfo.id = ?1 order by staff.employeeId")
    List<StaffEligibility> findBySchoolInfo(Long schoolInfoId);

    @Query("Select se from StaffEligibility se where se.staff.schoolInfo.id = ?1 and se.grade = ?2  order by staff.employeeId")
    List<StaffEligibility> findBySchoolInfoAndGrade(Long schoolInfoId, Grade grade);

    @Query("Select distinct se.staff.id from StaffEligibility se where se.staff.schoolInfo.id = ?1 and se.grade = ?2 and se.staff.id != ?3 order by staff.employeeId")
    List<Long> findAvailableStaffInSchoolByGrade(Long schoolInfoId, Grade grade, Long staffId);

    @Query("Select se from StaffEligibility se where se.staff.schoolInfo.id = ?1 and se.masterSubject = ?2 order by staff.employeeId")
    List<StaffEligibility> findBySchoolInfoAndSubject(Long schoolInfoId, MasterSubject masterSubject);

    @Query("Select se from StaffEligibility se where se.staff.schoolInfo.id = ?1 and se.masterSubject = ?2 and se.grade = ?3 order by staff.employeeId")
    List<StaffEligibility> findBySchoolInfoAndSubjectAndGrade(Long schoolInfoId, MasterSubject masterSubject, Grade grade);

    @Query("Select distinct se.staff.id from StaffEligibility se where se.staff.schoolInfo.id = ?1 and se.masterSubject = ?2 and se.staff.id != ?3 order by staff.employeeId")
    List<Long> findAvailableStaffInSchoolBySubject(Long schoolInfoId, MasterSubject masterSubject, Long staffId);

    @Query("Select distinct se.staff.id from StaffEligibility se where se.staff.schoolInfo.id = ?1 and se.masterSubject = ?2 and se.grade = ?3 and se.staff.id != ?4 order by staff.employeeId")
    List<Long> findAvailableStaffInSchoolBySubjectAndGrade(Long schoolInfoId, MasterSubject masterSubject, Grade grade, Long staffId);

    @Query("Select se from StaffEligibility se where se.staff.id = ?1")
    List<StaffEligibility> findByStaff(Long staffId);

    @Query("Select se from StaffEligibility se where se.staff.id = ?1 and se.grade = ?2")
    List<StaffEligibility> findByStaffAndGrade(Long staffId, Grade grade);

    @Query("Select se from StaffEligibility se where se.staff.id = ?1 and se.masterSubject = ?2")
    List<StaffEligibility> findByStaffAndSubject(Long staffId, MasterSubject masterSubject);

    @Query("Select se from StaffEligibility se where se.staff.id = ?1 and se.masterSubject = ?2 and se.grade = ?3")
    List<StaffEligibility> findByStaffAndSubjectAndGrade(Long staffId, MasterSubject masterSubject, Grade grade);

}
