package com.witcurve.repository;

import com.witcurve.domain.SurveyAnswer;
import com.witcurve.domain.SurveyQuestion;
import com.witcurve.domain.enumeration.QuestionType;
import com.witcurve.web.rest.vm.SurveyQuestionAnswerCountVM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {

    @Query("Select sa from SurveyAnswer sa where sa.question.section.form.id = ?1 and sa.user.id = ?2 order by sa.question.section.order, sa.question.order")
    List<SurveyAnswer> getByFormIdAndUserId(Long formId, Long userId);

    @Query("Select sa from SurveyAnswer sa where sa.question.section.id = ?1 and sa.user.id = ?2 order by sa.question.order")
    List<SurveyAnswer> getBySectionIdAndUserId(Long sectionId, Long userId);

    @Query(nativeQuery = true)
    List<SurveyQuestionAnswerCountVM> countForForm(List<String> types, Long formId);
}
