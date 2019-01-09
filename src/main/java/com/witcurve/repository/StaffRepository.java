package com.witcurve.repository;

import com.witcurve.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("select staff from Staff staff where staff.user.id = ?1")
    Staff getStaffByUserId(Long userId);

    @Query("select staff from Staff staff left join fetch staff.user usr where staff.schoolInfo.id = ?1 and lower(staff.staffId) = ?2")
    Staff findBySchoolInfoIdAndStaffId(Long schoolId, String staffId);

    @Query("select staff from Staff staff where staff.schoolInfo.school.id = ?1")
    List<Staff> findBySchoolId(Long schoolId);
}
