package com.witcurve.repository;

import com.witcurve.domain.Substitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubstitutionRepository extends JpaRepository<Substitution, Long> {

    @Query("select distinct sub.teacher.id from Substitution sub where sub.scd.gsd.id = ?1 " +
        "and sub.teacher.id in ?2 and sub.date = ?3")
    List<Long> alreadySubstitutedTeacherList(Long gsdId, List<Long> teacherIds, LocalDate date);

    @Query("select sub from Substitution sub where sub.date = ?1 and sub.scd.gsd.id in ?2")
    List<Substitution> findByDateAndGSDs(LocalDate date, List<Long> gsdIds);
}
