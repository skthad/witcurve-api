package com.witcurve.repository;

import com.witcurve.domain.Standard;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardRepository extends JpaRepository<Standard,Long> {

    @Query("select std from Standard std where std.classTeacher.id = ?1 and std.active = true")
    Standard findByClassTeacherId(Long classTeacherId);

    @Query("select std from Standard std where std.grade = ?1 and std.schoolInfo.id = ?2 and std.active = true order by std.section")
    List<Standard> findByGradeAndSchoolInfoId(Grade grade, Long schoolInfoId);

    @Query("select std from Standard std where std.schoolInfo.id = ?1 and std.active = true order by std.grade, std.section")
    List<Standard> findBySchoolInfoId(Long schoolInfoId);

    @Query("select std from Standard std where std.grade=?1 and std.section=?2 and std.schoolInfo.id = ?3")
    Standard findByGradeAndSectionAndSchoolInfoId(Grade grade, String section, Long schoolInfoId);

    @Query("select std from Standard std where std.schoolInfo.id = ?1 and std.active=true and std.id in (select gsd.standard.id from GeneralSlotDetails gsd where gsd.status='ACTIVE' and gsd.standard.id=std.id) order by std.grade, std.section")
    List<Standard> findSlotAssignedBySchoolInfoId(Long schoolInfoId);

    @Query("select std from Standard std where std.schoolInfo.id = ?1 and std.active=true and std.id not in (select gsd.standard.id from GeneralSlotDetails gsd where gsd.status='ACTIVE' and gsd.standard.id=std.id)  order by std.grade, std.section")
    List<Standard> findSlotUnassignedBySchoolInfoId(Long schoolInfoId);
}
