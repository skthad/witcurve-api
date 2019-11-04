package com.witcurve.repository;

import com.witcurve.domain.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolInfoRepository extends JpaRepository<SchoolInfo, Long> {

    @Query("select si from SchoolInfo si left join fetch si.school s left join fetch s.institute i order by si.primaryBoard desc, i.name, s.name, si.board ")
    List<SchoolInfo> findAll(Long instituteId);

    @Query("select si from SchoolInfo si left join fetch si.school s left join fetch s.institute i where si.school.institute.id = (select school.institute.id from SchoolInfo where id = ?1) order by si.primaryBoard desc, i.name, s.name, si.board ")
    List<SchoolInfo> findAllForInstutiteManager(Long schoolInfoId);

    @Query("select si from SchoolInfo si left join fetch si.school s left join fetch s.institute i where si.school.id = (select school.id from SchoolInfo where id = ?1) order by si.primaryBoard desc, i.name, s.name, si.board ")
    List<SchoolInfo> findAllForSchoolAdmin(Long schoolInfoId);

    @Query("select si from SchoolInfo si where si.school.id = ?1 order by  si.primaryBoard desc, si.board ")
    List<SchoolInfo> findBySchoolId(Long schoolId);

    @Modifying
    @Query("update SchoolInfo set primaryBoard = false where school.id = ?1")
    void deactivatePrimaryBoard(Long schoolId);
}
