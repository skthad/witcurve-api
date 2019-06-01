package com.witcurve.repository;

import com.witcurve.domain.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Long> {

    List<Guardian> findByStudentId(Long studentId);
}
