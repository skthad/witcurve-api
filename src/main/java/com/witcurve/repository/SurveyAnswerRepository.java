package com.witcurve.repository;

import com.witcurve.domain.SurveyAnswer;
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

    @Query(value = "select saa.answers from survey_answer sa inner join survey_answer_answers saa on sa.id=saa.survey_answer_id inner join survey_question sq on sq.id = sa.question_id \n" +
        "inner join survey_section sc on sc.id = sq.section_id where sa.question_id = ?1  and (SELECT 1 from survey_submission ss where ss.form_id = sc.form_id and ss.user_id = sa.user_id)", nativeQuery = true)
    List<String> getAllAnswersByQuestionId(Long questionId);

    @Query("Select new com.witcurve.web.rest.vm.SurveyQuestionAnswerCountVM(sa.question.id, count(sa.id)) from SurveyAnswer sa where sa.question.section.form.id = ?2 and sa.question.type in ?1 and EXISTS (SELECT ss FROM SurveySubmission ss WHERE ss.form.id = sa.question.section.form.id and ss.user.id = sa.user.id) group by sa.question.id order by sa.question.section.order, sa.question.order")
    List<SurveyQuestionAnswerCountVM> countForLongAndShortAnswerTypeQue(List<QuestionType> types, Long formId);
}
