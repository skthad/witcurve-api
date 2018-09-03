package com.witcurve.repository;

import com.witcurve.domain.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolInfoRepository extends JpaRepository<SchoolInfo, Long> {
}
