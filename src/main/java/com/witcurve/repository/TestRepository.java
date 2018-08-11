package com.witcurve.repository;

import com.witcurve.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    @Query("select t from Test t where t.postedDate = ?1 and t.timeTableUnit.id = ?2")
    List<Test> findTestsByPostedDateAndTimetableUnitId(LocalDate postedDate, Long timeTableUnitId);

    @Query("select t from Test t where t.testDate = ?1 and t.timeTableUnit.id = ?2")
    List<Test> findAllTestsByTestDateAndTimetableUnitId(LocalDate testDate, Long timeTableUnitId);
}

