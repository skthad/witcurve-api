package com.witcurve.repository;

import com.witcurve.domain.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {

    StudentClass findByStudentId(Long studentId);
}
