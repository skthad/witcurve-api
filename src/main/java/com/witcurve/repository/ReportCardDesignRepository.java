package com.witcurve.repository;

import com.witcurve.domain.ReportCardDesign;
import com.witcurve.domain.enumeration.ReportFieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportCardDesignRepository extends JpaRepository<ReportCardDesign, Long> {

    @Query("Select rcd from ReportCardDesign rcd where rcd.exam.id=?1 order by rcd.fieldType asc, rcd.order asc")
    List<ReportCardDesign> findByExam(Long examId);

    @Query("Select rcd from ReportCardDesign rcd where rcd.bindingId=?1 order by rcd.fieldType asc, rcd.order asc")
    List<ReportCardDesign> findByBindingId(String bindingId);

    @Query("Select rcd from ReportCardDesign rcd where rcd.fieldType=?1 and rcd.exam.id=?2 order by rcd.order asc")
    List<ReportCardDesign> findByFieldTypeAndExam(ReportFieldType fieldType, Long examId);

    @Query("Select rcd from ReportCardDesign rcd where rcd.fieldType=?1 and rcd.bindingId=?2 order by rcd.order asc")
    List<ReportCardDesign> findByFieldTypeAndBindingId(ReportFieldType fieldType, String bindingId);

    @Modifying
    @Query("delete from ReportCardDesign rcd where rcd.id in ?1")
    void deleteByIds(List<Long> ids);


}
