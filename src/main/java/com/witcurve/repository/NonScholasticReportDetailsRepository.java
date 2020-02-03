package com.witcurve.repository;

import com.witcurve.domain.NonScholasticReportDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NonScholasticReportDetailsRepository extends JpaRepository<NonScholasticReportDetails, Long> {

    @Modifying
    @Query("delete from NonScholasticReportDetails nsrd where nsrd.reportCardDesign.id in ?1")
    void deleteByRcdIds(List<Long> rcdIds);
}
