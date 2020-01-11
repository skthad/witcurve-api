package com.witcurve.repository;

import com.witcurve.domain.FeeDetails;
import com.witcurve.domain.enumeration.FeeDetailsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FeeDetailsRepository extends JpaRepository<FeeDetails, Long> {

    @Query("Select fd from FeeDetails fd where fd.schoolInfo.id = ?1 order by fd.createdDate")
    List<FeeDetails> findBySchoolInfoId(Long schoolInfoId);

    @Query("Select fd from FeeDetails fd where fd.schoolInfo.id = ?1 and fd.type = ?2 order by fd.createdDate")
    List<FeeDetails> findBySchoolInfoIdAndType(Long schoolInfoId, FeeDetailsType type);

    @Query("Select fd from FeeDetails fd where fd.name = ?1 and fd.schoolInfo.id = ?2 and fd.type = ?3")
    FeeDetails findByNameAndSchoolInfoAndType(String name, Long schoolInfoId, FeeDetailsType type);

    @Query("select count(fd) from FeeDetails fd where fd.type = ?2 and fd.id in ?3 and fd.schoolInfo.id = (select session.schoolInfo.id from AcademicSession session where  session.id = ?1 )")
    Long findCountByTypeAndFeeDescriptionIds(Long sessionId, FeeDetailsType type, Set<Long> feeDescriptionIds);
}
