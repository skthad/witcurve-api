package com.witcurve.repository;

import com.witcurve.domain.Substitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubstitutionRepository extends JpaRepository<Substitution, Long> {

    /*@Modifying
    @Query("update AcademicSession a set a.active = false where a.schoolInfo.id = ?1")
    void findByGsdAndTeacherOnDate(Long schoolInfoId);*/

    @Query("select distinct sub.teacher.id from Substitution sub where sub.scd.gsd.id = ?1 " +
        "and sub.teacher.id in ?2 and sub.date = ?3")
    List<Long> alreadySubstitutedTeacherList(Long gsdId, List<Long> teacherIds, LocalDate date);

    @Query("select sub from Substitution sub where sub.scd.id = ?1 and sub.date = ?2")
    Substitution getSubstitution(Long scdId, LocalDate date);
}
