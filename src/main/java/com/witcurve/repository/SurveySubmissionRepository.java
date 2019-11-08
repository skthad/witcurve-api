package com.witcurve.repository;

import com.witcurve.domain.SurveySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveySubmissionRepository extends JpaRepository<SurveySubmission, Long> {

    @Query("Select ss from SurveySubmission ss where ss.user.id = ?1")
    List<SurveySubmission> findByUserId(Long userId);

    @Query("Select ss from SurveySubmission ss where ss.form.id = ?1")
    List<SurveySubmission> findByFormId(Long formId);

    @Query("Select ss from SurveySubmission ss where ss.form.id = ?1 and ss.user.id = ?2")
    SurveySubmission findByFormAndUserId(Long formId, Long userId);

}
