package com.witcurve.repository;

import com.witcurve.domain.FeeDetails;
import com.witcurve.domain.enumeration.FeeDetailsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeDetailsRepository extends JpaRepository<FeeDetails, Long> {

    @Query("Select fd from FeeDetails fd where fd.schoolInfo.id = ?1 order by fd.createdDate")
    List<FeeDetails> findBySchoolInfoId(Long schoolInfoId);

    @Query("Select fd from FeeDetails fd where fd.schoolInfo.id = ?1 and fd.type = ?2 order by fd.createdDate")
    List<FeeDetails> findBySchoolInfoIdAndType(Long schoolInfoId, FeeDetailsType type);

}
