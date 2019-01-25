package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.LeaveApplyor;
import com.witcurve.domain.enumeration.Reason;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class LeaveApplicationDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Reason reason;

    private String description;

    @NotNull
    private LeaveApplyor type;

    private ApprovalStatus status = ApprovalStatus.PENDING;

    private Long approvedById;

    @NotNull
    private Long schoolInfoId;

    private Long appliedStaffId;

    private Long appliedStudentId;

    private Long appliedGuardianId;

    @NotNull
    private LocalDate fromLeaveDate;

    @NotNull
    private LocalDate toLeaveDate;

    private List<Long> eventIds;

    private Long numLeaveDays;

    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LeaveApplyor getType() {
        return type;
    }

    public void setType(LeaveApplyor type) {
        this.type = type;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long approvedById) {
        this.approvedById = approvedById;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

    public Long getAppliedStaffId() {
        return appliedStaffId;
    }

    public void setAppliedStaffId(Long appliedStaffId) {
        this.appliedStaffId = appliedStaffId;
    }

    public Long getAppliedStudentId() {
        return appliedStudentId;
    }

    public void setAppliedStudentId(Long appliedStudentId) {
        this.appliedStudentId = appliedStudentId;
    }

    public Long getAppliedGuardianId() {
        return appliedGuardianId;
    }

    public void setAppliedGuardianId(Long appliedGuardianId) {
        this.appliedGuardianId = appliedGuardianId;
    }

    public List<Long> getEventIds() {
        return eventIds;
    }

    public void setEventIds(List<Long> eventIds) {
        this.eventIds = eventIds;
    }

    public LocalDate getFromLeaveDate() {
        return fromLeaveDate;
    }

    public void setFromLeaveDate(LocalDate fromLeaveDate) {
        this.fromLeaveDate = fromLeaveDate;
    }

    public LocalDate getToLeaveDate() {
        return toLeaveDate;
    }

    public void setToLeaveDate(LocalDate toLeaveDate) {
        this.toLeaveDate = toLeaveDate;
    }

    public Long getNumLeaveDays() {
        return numLeaveDays;
    }

    public void setNumLeaveDays(Long numLeaveDays) {
        this.numLeaveDays = numLeaveDays;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaveApplicationDTO that = (LeaveApplicationDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "LeaveApplicationDTO{" +
            "id=" + id +
            ", reason=" + reason +
            ", description='" + description + '\'' +
            ", type=" + type +
            ", status=" + status +
            ", approvedById=" + approvedById +
            ", schoolInfoId=" + schoolInfoId +
            ", appliedStaffId=" + appliedStaffId +
            ", appliedStudentId=" + appliedStudentId +
            ", appliedGuardianId=" + appliedGuardianId +
            ", fromLeaveDate=" + fromLeaveDate +
            ", toLeaveDate=" + toLeaveDate +
            ", eventIds=" + eventIds +
            ", numLeaveDays=" + numLeaveDays +
            ", note='" + note + '\'' +
            '}';
    }
}
