package com.witcurve.repository;

import com.amazonaws.services.dynamodbv2.xspec.L;
import com.witcurve.domain.ScholasticReportDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholasticReportDetailsRepository extends JpaRepository<ScholasticReportDetails, Long> {

    @Modifying
    @Query("delete from ScholasticReportDetails srd where srd.reportCardDesign.id in ?1")
    void deleteByRcdIds(List<Long> rcdIds);
}
