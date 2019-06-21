package com.witcurve.domain;

import com.witcurve.service.util.ListToStringConverter;

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
    @Column(nullable = false)
    @Convert(converter = ListToStringConverter.class)
    private List<String> options;

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

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

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
