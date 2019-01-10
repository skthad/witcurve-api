package com.witcurve.repository;

import com.witcurve.domain.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardRepository extends JpaRepository<Standard,Long> {

    Standard findByClassTeacherIdAndTermId(Long classTeacherId, Long termId);

    @Query("select std from Standard std where std.schoolInfo.id = ?1")
    List<Standard> findBySchoolInfoId(Long schoolInfoId);
}
