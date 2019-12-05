package com.witcurve.repository;

import com.witcurve.domain.StudentFeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFeeStructureRepository extends JpaRepository<StudentFeeStructure, Long> {

    @Query("select sfs from StudentFeeStructure sfs where sfs.student.id = ?1 and sfs.session.id = ?2")
    StudentFeeStructure getByStudentIdAndSessionId(Long studentId, Long sessionId);

    @Query("select sfs from StudentFeeStructure sfs join StudentStandard ss on ss.student.id = sfs.student.id where ss.standard.id = ?1 and ss.active = true and sfs.session.id = ?2 order by ss.rollNo")
    List<StudentFeeStructure> getByStandardIdAndSessionId(Long standardId, Long sessionId);
}
