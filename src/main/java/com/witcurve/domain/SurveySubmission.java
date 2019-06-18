package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "survey_submission", uniqueConstraints = {
    @UniqueConstraint(name = "survey_form_user_id",
        columnNames = {"form_id", "user_id"})
})
public class SurveySubmission extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SurveyForm form;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SurveyForm getForm() {
        return form;
    }

    public void setForm(SurveyForm form) {
        this.form = form;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveySubmission that = (SurveySubmission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SurveySubmission{" +
            "id=" + id +
            '}';
    }
}
