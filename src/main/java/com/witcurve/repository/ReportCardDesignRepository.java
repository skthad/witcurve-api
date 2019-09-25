package com.witcurve.repository;

import com.witcurve.domain.ReportCardDesign;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ReportFieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportCardDesignRepository extends JpaRepository<ReportCardDesign, Long> {

    @Query("Select rcd from ReportCardDesign rcd where rcd.exam.id=?1 and rcd.grade = ?2 order by rcd.order asc, rcd.fieldType asc")
    List<ReportCardDesign> findByExamAndGrade(Long examId, Grade grade);

    @Query("Select rcd from ReportCardDesign rcd where rcd.fieldType=?1 and rcd.exam.id=?2 and rcd.grade=?3 order by rcd.order asc")
    List<ReportCardDesign> findByFieldTypeAndExamAndGrade(ReportFieldType fieldType, Long examId, Grade grade);

    @Modifying
    @Query("delete from ReportCardDesign rcd where rcd.id in ?1")
    void deleteByIds(List<Long> ids);

}
