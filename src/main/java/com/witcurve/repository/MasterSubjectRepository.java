package com.witcurve.repository;

import com.witcurve.domain.MasterSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterSubjectRepository extends JpaRepository<MasterSubject, Long> {

    @Query("select ms from MasterSubject ms where ms.name = ?1")
    MasterSubject findByName(String name);
}
