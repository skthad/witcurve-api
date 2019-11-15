package com.witcurve.repository;

import com.witcurve.domain.StudentFeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentFeeTypeRepository extends JpaRepository<StudentFeeType,Long> {


}
