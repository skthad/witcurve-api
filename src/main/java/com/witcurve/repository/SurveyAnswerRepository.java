package com.witcurve.repository;

import com.witcurve.domain.SurveyAnswer;
import com.witcurve.domain.SurveyForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {

    @Query("Select sa from SurveyAnswer sa where sa.question.section.form.id = ?1 and sa.user.id = ?2 order by sa.question.section.id, sa.question.id")
    List<SurveyAnswer> getByFormIdAndUserId(Long formId, Long userId);

    @Query("Select sa from SurveyAnswer sa where sa.question.section.id = ?1 and sa.user.id = ?2 order by sa.question")
    List<SurveyAnswer> getBySectionIdAndUserId(Long sectionId, Long userId);

    @Query("Select sq from SurveyQuestion sq where sq.required = true and sq.section.form.id = ?2 and sq.id not in (select sa.question.id from SurveyAnswer sa where sa.user.id = ?1 and sa.question.section.form.id = ?2)")
    List<SurveyAnswer> getMandatoryUnansweredRecordByUserIdAndFormId(Long userId, Long formId);
}
