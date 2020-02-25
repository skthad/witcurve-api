package com.witcurve.repository;

import com.witcurve.domain.SurveySubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface SurveySubmissionRepository extends JpaRepository<SurveySubmission, Long> {

    @Query("Select ss.form.id from SurveySubmission ss where ss.user.id=?1")
    List<Long> findByUserId(Long userId);

    @Query("Select ss from SurveySubmission ss where ss.form.id = ?1 order by ss.createdDate desc")
    List<SurveySubmission> findByFormId(Long formId);

    @Query("Select ss from SurveySubmission ss where ss.form.id = ?1 and ss.user.id = ?2")
    SurveySubmission findByFormIdAndUserId(Long formId, Long userId);

    @Query("Select ss from SurveySubmission ss where ss.user.id = ?1 order by ss.createdDate desc")
    List<SurveySubmission> getByUserId(Long userId);

    @Query("Select ss from SurveySubmission ss where ss.form.id = ?1 order by ss.createdDate desc")
    Page<SurveySubmission> findByFormIdUsingPageable(Long formId, Pageable pageable);

}
