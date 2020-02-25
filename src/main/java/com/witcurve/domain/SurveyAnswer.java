package com.witcurve.domain;

import com.witcurve.web.rest.vm.SurveyQuestionAnswerCountVM;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "survey_answer", uniqueConstraints = {
    @UniqueConstraint(name = "survey_question_user_id",
        columnNames = {"question_id", "user_id"})
})
@SqlResultSetMapping(
    name="getQuestionAndAnswersCount",
    classes={
        @ConstructorResult(
            targetClass= SurveyQuestionAnswerCountVM.class,
            columns={
                @ColumnResult(name="qid", type = Long.class),
                @ColumnResult(name="ans", type = String.class),
                @ColumnResult(name = "ansc", type = Long.class)
            }
        )
    }
)

@NamedNativeQuery(name="SurveyAnswer.countForForm", query="SELECT sq.id as qid, saa.answers as ans, count(saa.answers) as ansc FROM survey_answer as sa \n" +
    "inner join survey_answer_answers as saa on sa.id=saa.survey_answer_id\n" +
    "inner join survey_question as sq on sq.id=sa.question_id\n" +
    "inner join survey_section as sc on sc.id=sq.section_id\n" +
    "inner join survey_submission as ss on ss.user_id = sa.user_id\n"+
    "where sq.type in ?1 and sc.form_id=?2 and ss.form_id = ?2\n" +
    "group by sq.id, saa.answers order by sc.section_order asc, sq.question_order asc;", resultSetMapping="getQuestionAndAnswersCount")
public class SurveyAnswer extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "answers", length = 500)
    @CollectionTable(name = "survey_answer_answers", joinColumns=@JoinColumn(name="survey_answer_id"))
    private List<String> answers;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SurveyQuestion question;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getAnswers() { return answers; }

    public void setAnswers(List<String> answers) { this.answers = answers; }

    public SurveyQuestion getQuestion() {
        return question;
    }

    public void setQuestion(SurveyQuestion question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
