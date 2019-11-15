package com.witcurve.repository;

import com.witcurve.domain.StudentFeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentFeeStructureRepository extends JpaRepository<StudentFeeStructure,Long> {
}
