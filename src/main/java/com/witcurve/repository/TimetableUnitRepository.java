package com.witcurve.repository;

import com.witcurve.domain.TimeTableUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableUnitRepository extends JpaRepository<TimeTableUnit, Long> {

    @Query("select ttu from TimeTableUnit ttu where ttu.courseTeacher.standard.id = ?1")
    List<TimeTableUnit> findByClassId(Long classId);
}
