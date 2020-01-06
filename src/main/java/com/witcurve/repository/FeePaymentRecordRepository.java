package com.witcurve.repository;

import com.witcurve.domain.FeePaymentRecord;
import com.witcurve.domain.enumeration.FeePaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeePaymentRecordRepository extends JpaRepository<FeePaymentRecord, Long> {

    @Query("select fpr from FeePaymentRecord fpr where fpr.studentFeeStructure.student.id = ?1 and  fpr.studentFeeStructure.session.id = ?2 order by fpr.createdDate")
    List<FeePaymentRecord> getByStudentAndSessionId(Long studentId, Long sessionId);

    @Query("select fpr from FeePaymentRecord fpr join StudentStandard ss on ss.student.id = fpr.studentFeeStructure.student.id where ss.standard.id = ?1 and ss.active " +
        "= true and fpr.studentFeeStructure.session.id = ?2 order by ss.rollNo, fpr.createdDate")
    List<FeePaymentRecord> getByStandardAndSessionId(Long standardId, Long sessionId);

    //check do we need to check with studentId also as orderId is unique
    @Query("select fpr from FeePaymentRecord fpr join fpr.feePaymentDetails fprfd where fpr.studentFeeStructure.student.id = ?1 and fpr.transactionId = ?2 and fpr.feePaymentType = ?5 and fprfd.feeType.id = ?3 and " +
        "fprfd.feeDescription.id = ?4")
    FeePaymentRecord getByStudentIdOrderIdAndFeeNameAndType(Long studentId, String orderId, Long feeTypeId, Long feeDescriptionId, FeePaymentType type);

    @Query("select fpr from FeePaymentRecord fpr where fpr.studentFeeStructure.student.id = ?1 and  fpr.studentFeeStructure.session.id = ?2 and fpr.feePaymentType = ?3 order by fpr.createdDate")
    List<FeePaymentRecord> getByStudentAndSessionIdAndType(Long studentId, Long sessionId, FeePaymentType type);

    @Query("select fpr from FeePaymentRecord fpr where fpr.transactionId = ?1 and fpr.feePaymentType = ?2")
    FeePaymentRecord getByTransactionId(String transactionId, FeePaymentType feePaymentType);
}
