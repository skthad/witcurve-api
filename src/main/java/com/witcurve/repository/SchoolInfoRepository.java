package com.witcurve.repository;

import com.witcurve.domain.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolInfoRepository extends JpaRepository<SchoolInfo, Long> {

    @Query("select si from SchoolInfo si where si.school.id = ?1")
    List<SchoolInfo> findBySchoolId(Long schoolId);
}
