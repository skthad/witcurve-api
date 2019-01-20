package com.witcurve.repository;

import com.witcurve.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    @Query("select s from School s where s.institute.id = ?1")
    List<School> findByInstituteId(Long instituteId);
}
