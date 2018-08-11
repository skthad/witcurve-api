package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class LeaveDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate postedDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate leaveDate;

    @NotNull
    private String reason;

    private Boolean approved;

    private Boolean markedAbsent;

    private Long approverId;

    private String leaveGroupId;

    @NotNull
    private Long studentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getMarkedAbsent() {
        return markedAbsent;
    }

    public void setMarkedAbsent(Boolean markedAbsent) {
        this.markedAbsent = markedAbsent;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public String getLeaveGroupId() {
        return leaveGroupId;
    }

    public void setLeaveGroupId(String leaveGroupId) {
        this.leaveGroupId = leaveGroupId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaveDTO)) return false;
        LeaveDTO leaveDTO = (LeaveDTO) o;
        return Objects.equals(getId(), leaveDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "LeaveDTO{" +
            "id=" + id +
            ", postedDate=" + postedDate +
            ", leaveDate=" + leaveDate +
            ", reason='" + reason + '\'' +
            ", approved=" + approved +
            ", markedAbsent=" + markedAbsent +
            ", approverId=" + approverId +
            ", leaveGroupId='" + leaveGroupId + '\'' +
            ", studentId=" + studentId +
            '}';
    }
}
