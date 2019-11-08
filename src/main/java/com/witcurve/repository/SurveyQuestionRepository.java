package com.witcurve.repository;

import com.witcurve.domain.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {

    @Query("Select sq from SurveyQuestion sq where sq.section.form.id = ?1")
    List<SurveyQuestion> getByFormId(Long formId);

    @Query("Select sq from SurveyQuestion sq where sq.section.id = ?1")
    List<SurveyQuestion> getBySectionId(Long sectionId);

    @Query("Select sq from SurveyQuestion sq where sq.section.form.id = ?1 and sq.required = true")
    List<Long> getMandatoryQuestionsByFormId(Long formId);
}
