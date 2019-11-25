package com.witcurve.repository;

import com.witcurve.domain.StudentFeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFeeStructureRepository extends JpaRepository<StudentFeeStructure,Long> {

    @Query("select sfs from StudentFeeStructure sfs where sfs.student.id = ?1 and sfs.session.id = ?2")
    StudentFeeStructure getByStudentIdAndSessionId(Long studentId, Long sessionId);

    @Query("select sfs from StudentFeeStructure sfs where sfs.student.id in (select ss.student.id from StudentStandard ss where ss.standard.id = ?1 order by ss.rollNo asc) and sfs.session.id = ?2")
    List<StudentFeeStructure> getByStandardIdAndSessionId(Long standardId, Long sessionId);
}
