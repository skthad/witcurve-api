package com.witcurve.repository;

import com.witcurve.domain.StandardReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardReportRepository extends JpaRepository<StandardReport, Long> {


    @Query("Select sr from StandardReport sr where sr.reportCard.exam.id=?1 order by sr.standard.grade asc, sr.standard.section asc")
    List<StandardReport> findByExamId(Long examId);

    StandardReport findByStandardIdAndReportCardId(Long standardId, Long reportCardId);

}
