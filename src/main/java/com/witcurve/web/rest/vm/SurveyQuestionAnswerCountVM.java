package com.witcurve.web.rest.vm;

import java.math.BigInteger;
import java.util.Objects;

public class SurveyQuestionAnswerCountVM {

    private Long questionId;

    private String answer;

    private Long count;

    public SurveyQuestionAnswerCountVM() {

    }

    public SurveyQuestionAnswerCountVM(Long questionId, Long count) {
        this.questionId = questionId;
        this.count = count;
    }

    public SurveyQuestionAnswerCountVM(Long questionId, String answer, Long count) {
        this.questionId = questionId;
        this.answer = answer;
        this.count = count;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyQuestionAnswerCountVM that = (SurveyQuestionAnswerCountVM) o;
        return answer == that.answer &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, answer, count);
    }
}
