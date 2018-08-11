package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="leave")
public class Leave extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leaveIdSeq")
    @SequenceGenerator(name = "leaveIdSeq", sequenceName="leave_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "posted_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate postedDate;

    @NotNull
    @Column(name = "leave_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate leaveDate;

    @NotNull
    @Column(name = "reason", nullable = false )
    private String reason;

    @Column(name = "approved")
    private Boolean approved;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Staff approvedBy;

    @Column(name = "marked_absent")
    private Boolean markedAbsent;

    @Column(name = "leave_group_id")
    private String leaveGroupId;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false, name = "student_id")
    private Student student;

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

    public Staff getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Staff approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Boolean getMarkedAbsent() {
        return markedAbsent;
    }

    public void setMarkedAbsent(Boolean markedAbsent) {
        this.markedAbsent = markedAbsent;
    }

    public String getLeaveGroupId() {
        return leaveGroupId;
    }

    public void setLeaveGroupId(String leaveGroupId) {
        this.leaveGroupId = leaveGroupId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leave)) return false;
        Leave leave = (Leave) o;
        return Objects.equals(getId(), leave.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Leave{" +
            "id=" + id +
            ", postedDate=" + postedDate +
            ", leaveDate=" + leaveDate +
            ", reason='" + reason + '\'' +
            ", approved=" + approved +
            ", approvedBy=" + approvedBy +
            ", markedAbsent=" + markedAbsent +
            ", leaveGroupId='" + leaveGroupId + '\'' +
            ", student=" + student +
            '}';
    }
}
