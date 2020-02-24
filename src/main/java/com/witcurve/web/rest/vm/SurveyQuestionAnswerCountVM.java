package com.witcurve.web.rest.vm;

import java.math.BigInteger;
import java.util.Objects;

public class SurveyQuestionAnswerCountVM {

    private BigInteger questionId;

    private String answer;

    private Integer count;

    public SurveyQuestionAnswerCountVM() {

    }

    public SurveyQuestionAnswerCountVM(BigInteger questionId, String answer, Integer count) {
        this.questionId = questionId;
        this.answer = answer;
        this.count = count;
    }

    public BigInteger getQuestionId() {
        return questionId;
    }

    public void setQuestionId(BigInteger questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
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
