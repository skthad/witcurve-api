package com.witcurve.repository;

import com.witcurve.domain.SessionFeeDescription;
import com.witcurve.domain.SessionFeeStructure;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionFeeStructureRepository extends JpaRepository<SessionFeeStructure, Long> {

    @Query("Select sfs from SessionFeeStructure sfs where sfs.grade = ?1 and sfs.session.id = ?2")
    List<SessionFeeStructure> findByGradeAndSessionId(Grade grade, Long sessionId);

    @Query("Select sfs from SessionFeeStructure sfs join sfs.sessionFeeDescriptions sfssfd where sfs.feeType.id = ?1 or sfssfd.feeDescription.id = ?1")
    List<SessionFeeStructure> findByFeeTypeAndFeeDescriptionId(Long feeDetailsId);

    @Query("select sfs from SessionFeeStructure sfs where sfs.grade = ?1 and sfs.session.schoolInfo.id = ?2")
    List<SessionFeeStructure> findByGradeAndSchoolInfoId(Grade grade, Long schoolInfoId);

    @Query("Select sfs from SessionFeeStructure sfs where sfs.grade in (select ss.standard.grade from StudentStandard ss where ss.student.id = ?1 and ss.active = true) and sfs.session.id = ?2")
    List<SessionFeeStructure> findByStudentIdAndSessionId(Long studentId, Long sessionId);
}
