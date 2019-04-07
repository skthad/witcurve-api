package com.witcurve.repository;

import com.witcurve.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("select staff from Staff staff where staff.user is not null and staff.user.id = ?1")
    Staff getStaffByUserId(Long userId);

    @Query("select staff from Staff staff left join fetch staff.user usr where staff.schoolInfo.id = ?1 and lower(staff.employeeId) = ?2")
    Staff findBySchoolInfoIdAndStaffId(Long schoolId, String staffId);

    @Query("select staff from Staff staff where staff.schoolInfo.school.id = ?1 order by staff.schoolInfo.school.name, staff.employeeId")
    List<Staff> findBySchoolId(Long schoolId);

    @Query("select staff from Staff staff where staff.schoolInfo.id = ?1 and staff.user.id in (select id from User where activated = true) order by staff.employeeId")
    List<Staff> findBySchoolInfoId(Long schoolInfoId);

    @Query("select staff from Staff staff where staff.schoolInfo.id = ?1 and staff.id in (Select s.classTeacher.id from Standard s where s.classTeacher.id=staff.id) order by staff.employeeId")
    List<Staff> findClassTeachersBySchoolInfoId(Long schoolInfoId);

    @Query("select distinct staff.primaryPhone from Staff staff where staff.schoolInfo.id = ?1 and staff.user.activated = true")
    Set<String> findActiveStaffPhoneNumbersInSchoolInfoId(Long schoolInfoId);

    @Query("select distinct staff.primaryPhone from Staff staff where staff.schoolInfo.id = ?1 and staff.id in ?2")
    Set<String> getPhoneNumbersBySchoolInfoAndStaffIds(Long schoolInfoId, List<Long> staffIds);

}
