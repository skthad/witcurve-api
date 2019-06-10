package com.witcurve.web.rest.vm;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * View Model object for storing Email request
 */
public class EmailVM extends SmsVM {

    @NotNull
    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailVM emailVM = (EmailVM) o;
        return Objects.equals(subject, emailVM.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject);
    }

    @Override
    public String toString() {
        return "EmailVM{" +
            ", subject='" + subject + '\'' +
            '}';
    }
}
