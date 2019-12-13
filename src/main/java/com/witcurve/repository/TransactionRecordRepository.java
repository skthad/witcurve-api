package com.witcurve.repository;

import com.witcurve.domain.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {

    @Query("select tr from TransactionRecord tr where tr.schoolInfo.id = ?1 and tr.transactionDate between ?2 and ?3 order by tr.transactionDate")
    List<TransactionRecord> getBySchoolInfoAndTransactionDate(Long schoolInfoId, LocalDate fromDate, LocalDate endDate);

}
