package com.witcurve.repository;

import com.witcurve.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("select staff from Staff staff where staff.user.id = ?1")
    Staff getStaffByUserId(Long userId);
}
