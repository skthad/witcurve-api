package com.witcurve.repository;

import com.witcurve.domain.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {

    @Query("Select sq from SurveyQuestion sq where sq.section.form.id = ?1 order by sq.section.order , sq.order")
    List<SurveyQuestion> getByFormId(Long formId);

    @Query("Select sq from SurveyQuestion sq where sq.section.id = ?1 order by sq.order")
    List<SurveyQuestion> getBySectionId(Long sectionId);

}
