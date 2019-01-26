package com.witcurve.repository;

import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.domain.enumeration.GSDStatus;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface GeneralSlotDetailsRepository extends JpaRepository<GeneralSlotDetails, Long> {

    @Query("select gsd from GeneralSlotDetails gsd where gsd.standard.id = ?1 " +
        "and gsd.exam.id is null order by bindingId, start")
    List<GeneralSlotDetails> findGSDsByStandardId(Long standardId);

    @Query("select gsd from GeneralSlotDetails gsd where gsd.standard.id = ?1 " +
        "and gsd.exam.id is null and status = ?2 order by bindingId, start")
    List<GeneralSlotDetails> findGSDsByStandardIdAndStatus(Long standardId, GSDStatus status);

    @Query("select gsd from GeneralSlotDetails gsd where gsd.grade = ?1 " +
        "and gsd.exam.id = ?2 order by bindingId, start")
    List<GeneralSlotDetails> findExamSlotsByGradeAndExamId(Grade grade,Long examId);

    @Modifying
    @Query("update GeneralSlotDetails set status = 'ACTIVE' where " +
        "standard.id = ?1 and bindingId = ?2")
    void activateSlotDetailsForStandard(Long standardId, String bindingId);

    @Modifying
    @Query("update GeneralSlotDetails set status = 'INACTIVE' where " +
        "standard.id in ?1 and exam.id is null and status = 'ACTIVE'")
    void deactivateSlotDetailsForStandards(Set<Long> standardIds);

    @Modifying
    @Query("delete from GeneralSlotDetails where bindingId = ?1")
    void deleteByBindingId(String bindingId);

    @Modifying
    @Query("delete from GeneralSlotDetails where exam.id = ?1")
    void deleteByExamId(Long examId);

    @Modifying
    @Query("delete from GeneralSlotDetails where grade = ?1 and exam.id = ?2")
    void deleteByGradeAndExamId(Grade grade, Long examId);
}
