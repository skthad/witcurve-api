package com.witcurve.repository;

import com.witcurve.domain.SurveySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveySubmissionRepository extends JpaRepository<SurveySubmission, Long> {

}
