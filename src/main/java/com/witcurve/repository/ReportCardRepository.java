package com.witcurve.repository;

import com.witcurve.domain.ReportCard;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportCardRepository extends JpaRepository<ReportCard, Long> {

    @Query("Select rc from ReportCard rc where rc.exam.id=?1")
    List<ReportCard> findByExamId(Long examId);

    @Query("Select rc from ReportCard rc where rc.exam.id=?1 and rc.grade=?2")
    ReportCard findByExamIdAndGrade(Long examId, Grade grade);
}
