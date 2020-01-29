package com.witcurve.repository;

import com.witcurve.domain.TransactionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {

    @Query("select tr from TransactionRecord tr where tr.schoolInfo.id = ?1 and tr.transactionDate between ?2 and ?3 order by tr.transactionDate")
    Page<TransactionRecord> getBySchoolInfoAndTransactionDate(Long schoolInfoId, LocalDate fromDate, LocalDate endDate, Pageable pageable);

}
