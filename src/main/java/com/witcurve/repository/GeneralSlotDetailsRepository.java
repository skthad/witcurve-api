package com.witcurve.repository;

import com.witcurve.domain.GeneralSlotDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneralSlotDetailsRepository extends JpaRepository<GeneralSlotDetails, Long> {

    List<GeneralSlotDetails> findByStandardIdAndExamIdNull(Long standardId);
}
