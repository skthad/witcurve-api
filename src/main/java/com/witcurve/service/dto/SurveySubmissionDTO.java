package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SurveySubmissionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long formId;

    private String userName;

    private List<SurveyAnswerDTO> answers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public List<SurveyAnswerDTO> getAnswers() { return answers; }

    public void setAnswers(List<SurveyAnswerDTO> answers) { this.answers = answers; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveySubmissionDTO that = (SurveySubmissionDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SurveySubmissionDTO{" +
            "id=" + id +
            '}';
    }
}
