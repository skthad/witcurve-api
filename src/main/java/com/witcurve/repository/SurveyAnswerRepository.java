package com.witcurve.repository;

import com.witcurve.domain.SurveyAnswer;
import com.witcurve.domain.SurveyForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {

    @Query("Select sa from SurveyAnswer sa where sa.question.section.form.id = ?1 and sa.user.id = ?2")
    List<SurveyAnswer> getByFormIdAndUserId(Long formId, Long userId);

    @Query("Select sa from SurveyAnswer sa where sa.question.section.id = ?1 and sa.user.id = ?2")
    List<SurveyAnswer> getBySectionIdAndUserId(Long sectionId, Long userId);

    @Query("Select sa.question.section.form from SurveyAnswer sa where sa.id = ?1")
    SurveyForm getSurveyFormByAnsId(Long ansId);

    @Query("Select sa from SurveyAnswer sa where sa.question.section.form.id = ?1 and sa.user.id = ?2 and sa.question.required = true")
    List<SurveyAnswer> getMandatoryUnansweredAnswerByFormIdAndUserId(Long formId, Long userId);

    @Query("Select sa from SurveyAnswer sa where sa.question.id in ?1 and sa.user.id = ?2")
    List<SurveyAnswer> getByQuestionIdAndUserId(List<Long> questionId, Long userId);
}
