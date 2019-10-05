package com.witcurve.service.dto;

import com.witcurve.domain.Attachment;
import com.witcurve.domain.enumeration.Grade;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class StudentReportDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Long reportCardId;

    @NotNull
    private Long studentId;

    @NotNull
    private Attachment attachment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportCardId() {
        return reportCardId;
    }

    public void setReportCardId(Long reportCardId) {
        this.reportCardId = reportCardId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentReportDTO)) return false;
        StudentReportDTO that = (StudentReportDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudentReportDTO{" +
            "id=" + id +
            ", reportCardId=" + reportCardId +
            ", studentId=" + studentId +
            ", attachment=" + attachment +
            '}';
    }
}
