package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.Reason;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class LeaveApplicationDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Long schoolInfoId;

    private Long appliedStaffId;

    private String staffName;

    private Long appliedStudentId;

    private String studentName;

    private String rollNo;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate fromLeaveDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate toLeaveDate;

    @NotNull
    private Reason reason;

    private String description;

    private ApprovalStatus status = ApprovalStatus.PENDING;

    private Long approvedById;

    private List<Long> eventIds;

    private Long numLeaveDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Long getAppliedStudentId() {
        return appliedStudentId;
    }

    public void setAppliedStudentId(Long appliedStudentId) {
        this.appliedStudentId = appliedStudentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
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

    public List<Long> getEventIds() {
        return eventIds;
    }

    public void setEventIds(List<Long> eventIds) {
        this.eventIds = eventIds;
    }

    public Long getNumLeaveDays() {
        return numLeaveDays;
    }

    public void setNumLeaveDays(Long numLeaveDays) {
        this.numLeaveDays = numLeaveDays;
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
            '}';
    }
}
