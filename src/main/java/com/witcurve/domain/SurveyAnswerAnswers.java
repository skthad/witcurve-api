package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "survey_answer_answers")
public class SurveyAnswerAnswers implements Serializable {

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SurveyAnswer surveyAnswer;

    @Id
    @NotNull
    @Column(name = "answers", length = 255)
    private String answers;

    public SurveyAnswer getSurveyAnswer() { return surveyAnswer; }

    public void setSurveyAnswer(SurveyAnswer surveyAnswer) { this.surveyAnswer = surveyAnswer; }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyAnswerAnswers that = (SurveyAnswerAnswers) o;
        return Objects.equals(surveyAnswer, that.surveyAnswer) &&
            Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surveyAnswer, answers);
    }

    @Override
    public String toString() {
        return "SurveyAnswerAnswers{" +
            "surveyAnswer=" + surveyAnswer +
            ", answers='" + answers + '\'' +
            '}';
    }
}
