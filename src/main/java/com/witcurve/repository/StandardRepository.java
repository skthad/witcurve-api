package com.witcurve.repository;

import com.witcurve.domain.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardRepository extends JpaRepository<Standard,Long> {

    Standard findByClassTeacherId(Long classTeacherId);

    @Query("select std from Standard std where std.schoolInfo.id = ?1")
    List<Standard> findBySchoolInfoId(Long schoolInfoId);

    @Query("select std from Standard std where std.schoolInfo.id = ?1 and std.id in (select gsd.standard.id from GeneralSlotDetails gsd where gsd.status='ACTIVE' and gsd.standard.id=std.id)")
    List<Standard> findSlotAssignedBySchoolInfoId(Long schoolInfoId);

    @Query("select std from Standard std where std.schoolInfo.id = ?1 and std.id not in (select gsd.standard.id from GeneralSlotDetails gsd where gsd.status='ACTIVE' and gsd.standard.id=std.id)")
    List<Standard> findSlotUnassignedBySchoolInfoId(Long schoolInfoId);
}
