package com.witcurve.repository;

import com.witcurve.domain.SurveySection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveySectionRepository extends JpaRepository<SurveySection, Long> {

}
