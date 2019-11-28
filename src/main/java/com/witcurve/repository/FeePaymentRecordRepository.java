package com.witcurve.repository;

import com.witcurve.domain.FeePaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeePaymentRecordRepository extends JpaRepository<FeePaymentRecord, Long> {

    @Query("select fpr from FeePaymentRecord fpr where fpr.studentFeeStructure.student.id = ?1 and  fpr.studentFeeStructure.session.id = ?2 order by fpr.createdDate")
    List<FeePaymentRecord> getByStudentAndSessionId(Long studentId, Long sessionId);

    @Query("select fpr from FeePaymentRecord fpr join StudentStandard ss on ss.student.id = fpr.studentFeeStructure.student.id where ss.standard.id = ?1 and fpr.studentFeeStructure.session.id = ?2 order by ss.rollNo, fpr.createdDate")
    List<FeePaymentRecord> getByStandardAndSessionId(Long standardId, Long sessionId);
}
