package com.witcurve.repository;

import com.witcurve.domain.SurveySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveySubmissionRepository extends JpaRepository<SurveySubmission, Long> {

    @Query("Select ss.form.id from SurveySubmission ss where ss.user.id=?1 and ss.form.id=?2")
    List<Long> findByUserId(Long userId);

}
