package com.witcurve.repository;

import com.witcurve.domain.StudentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentReportRepository extends JpaRepository<StudentReport, Long> {

    @Query("select sr from StudentReport sr where sr.student.id=?1 order by sr.reportCard.exam.startDate asc")
    List<StudentReport> findByStudentId(Long studentId);

}
