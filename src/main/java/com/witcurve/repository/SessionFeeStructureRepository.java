package com.witcurve.repository;

import com.witcurve.domain.SessionFeeStructure;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionFeeStructureRepository extends JpaRepository<SessionFeeStructure, Long> {

    @Query("Select sfs from SessionFeeStructure sfs where sfs.grade = ?1 and sfs.session.id in (select a from AcademicSession session where session.schoolInfo.id = ?1 and session.startDate = (select max(session.startDate) from AcademicSession session where session.active = true and session.schoolInfo.id = ?1 and session.startDate <= ?2))")
    List<SessionFeeStructure> findByGradeAndSchoolInfoId(Grade grade, Long schoolInfoId);

    @Query("Select sfs from SessionFeeStructure sfs where sfs.feeType.id =1? or sfs.feeDescription.id = ?1")
    List<SessionFeeStructure>  findByFeeDetailsId(Long feeDetailsId);
}
