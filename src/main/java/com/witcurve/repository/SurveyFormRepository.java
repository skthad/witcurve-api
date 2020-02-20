package com.witcurve.repository;

import com.witcurve.domain.SurveyForm;
import com.witcurve.domain.enumeration.QuestionType;
import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.domain.enumeration.SurveyUserType;
import com.witcurve.web.rest.vm.SurveyQuestionAnswerCountVM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyFormRepository extends JpaRepository<SurveyForm, Long> {

    @Query("Select sf from SurveyForm  sf where sf.schoolInfo.id=?1 and sf.creator=?2 and sf.status in ?3")
    List<SurveyForm> findBySchoolInfoIdAndCreatorAndStatusList(Long schoolInfoId, SurveyFormCreator creator, List<SurveyFormStatus> statuses);

    @Query("Select sf from SurveyForm  sf where sf.schoolInfo.id=?1 and sf.type in ?2 and sf.status in ?3")
    List<SurveyForm> findBySchoolInfoIdAndTypesAndStatusList(Long schoolInfoId, List<SurveyUserType> types, List<SurveyFormStatus> statuses);

    @Query("Select sf from SurveyForm sf left join SurveyFormStandards sfs on sfs.surveyFormId = sf.id where  sf.status in ?2 and sf.schoolInfo.id = ?1 and ((sf.type = 'ALL') or (sf.type = 'PARENT' and (sfs.standardId = ?3 or sfs.standardId is null)))")
    List<SurveyForm> findBySchoolInfoIdAndStatusAndStandardId(Long schoolInfoId, List<SurveyFormStatus> statuses, Long standardId);

    @Query("Select new com.witcurve.web.rest.vm.SurveyQuestionAnswerCountVM(sq.id, saa.answers, count(saa.answers)) from SurveyQuestion sq join SurveyAnswer sa on sa.question.id = sq.id join SurveyAnswerAnswers saa on saa.surveyAnswer.id = sa.id join SurveySubmission ss on sa.user.id = ss.user.id where sq.type in ?1 and  sq.section.form.id = ?2  group by sq.id, saa.answers order by sq.section.order asc, sq.order asc ")
    List<SurveyQuestionAnswerCountVM> getQuestionAndAnswersCount(List<QuestionType> types, Long formId);
}
