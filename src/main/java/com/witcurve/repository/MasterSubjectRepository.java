package com.witcurve.repository;

import com.witcurve.domain.MasterSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterSubjectRepository extends JpaRepository<MasterSubject, Long> {
}
