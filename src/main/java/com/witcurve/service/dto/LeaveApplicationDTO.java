package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.LeaveApplyor;
import com.witcurve.domain.enumeration.Reason;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class LeaveApplicationDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Reason reason;

    private String description;

    @NotNull
    private LeaveApplyor type;

    @NotNull
    private Boolean approved = false;

    private Long approvedById;

    private Long eventId;

    @NotNull
    private Long sessionId;

    private Long appliedStaffId;

    private Long appliedStudentId;

    private Long appliedGuardianId;

    @NotNull
    private LocalDate leaveDate;

    private String bindingId;

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

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long approvedById) {
        this.approvedById = approvedById;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
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

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaveApplicationDTO that = (LeaveApplicationDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LeaveApplicationDTO{" +
            "id=" + id +
            ", reason=" + reason +
            ", description='" + description + '\'' +
            ", type='" + type + '\'' +
            ", approved=" + approved +
            ", approvedById=" + approvedById +
            ", eventId=" + eventId +
            ", sessionId=" + sessionId +
            ", appliedStaffId=" + appliedStaffId +
            ", appliedStudentId=" + appliedStudentId +
            ", appliedGuardianId=" + appliedGuardianId +
            ", leaveDate=" + leaveDate +
            ", bindingId='" + bindingId + '\'' +
            '}';
    }
}
