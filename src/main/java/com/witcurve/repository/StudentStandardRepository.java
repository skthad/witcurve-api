package com.witcurve.repository;

import com.witcurve.domain.StudentStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentStandardRepository extends JpaRepository<StudentStandard, Long> {

    StudentStandard findByStudentId(Long studentId);
}
