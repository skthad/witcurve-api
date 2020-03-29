package com.witcurve.service.dto;

import com.witcurve.domain.Attachment;

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

    @NotNull Boolean viewable = true;

    private String examName;

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

    public Boolean getViewable() {
        return viewable;
    }

    public void setViewable(Boolean viewable) {
        this.viewable = viewable;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
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
            '}';
    }
}
