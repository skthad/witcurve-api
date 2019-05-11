package com.witcurve.repository;

import com.witcurve.domain.MobileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileMetaDataRepository extends JpaRepository<MobileMetaData, Long> {

    @Query("select mmd from MobileMetaData mmd where mmd.institute.id=?1")
    List<MobileMetaData> findByInstituteId(Long instituteId);

    @Modifying
    @Query("delete from MobileMetaData mmd where mmd.institute.id=?1")
    void deleteByInstituteId(Long instituteId);

}
