package com.witcurve.repository;

import com.witcurve.domain.ReportCardDesign;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.domain.enumeration.ReportModelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportCardDesignRepository extends JpaRepository<ReportCardDesign, Long> {

    @Query("Select rcd from ReportCardDesign rcd where rcd.modelType=?1 and rcd.schoolInfo.id=?2 and rcd.activated=true order by rcd.fieldType asc, rcd.order asc")
    List<ReportCardDesign> findByModelTypeAndSchoolInfo(ReportModelType type, Long schoolInfoId);

    @Query("Select rcd from ReportCardDesign rcd where rcd.modelType=?1 and rcd.fieldType=?2 and rcd.schoolInfo.id=?3 and rcd.activated=true order by rcd.order asc")
    List<ReportCardDesign> findByModelTypeAndFieldTypeAndSchoolInfo(ReportModelType type, ReportFieldType fieldType, Long schoolInfoId);

    @Modifying
    @Query("update ReportCardDesign rcd set rcd.activated=false where rcd.id in ?1")
    void deactivateReportCardDesigns(List<Long> ids);



}
