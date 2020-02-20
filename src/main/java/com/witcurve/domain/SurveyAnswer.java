package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "survey_answer", uniqueConstraints = {
    @UniqueConstraint(name = "survey_question_user_id",
        columnNames = {"question_id", "user_id"})
})
public class SurveyAnswer extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "answers", columnDefinition = "varchar(255)")
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
