package com.witcurve.repository;

import com.witcurve.domain.Student;
import com.witcurve.domain.StudentRemarks;
import com.witcurve.domain.StudentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentReportRepository extends JpaRepository<StudentReport, Long> {

    @Query("select studentReport from StudentReport studentReport where studentReport.student.id=1?")
    List<StudentReport> findByStudentId(Long studentId);

}
