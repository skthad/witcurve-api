package com.witcurve.domain;

import com.google.gag.annotation.remark.OhNoYouDidnt;
import com.witcurve.domain.enumeration.LeaveApplyor;
import com.witcurve.domain.enumeration.Reason;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="leave_application")
public class LeaveApplication extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leaveApplicationIdSeq")
    @SequenceGenerator(name = "leaveApplicationIdSeq", sequenceName="leave_application_id_seq", allocationSize = 0)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private Reason reason;

    @Column
    private String description;

    @NotNull
    @Column(nullable =false)
    private LeaveApplyor type;

    @NotNull
    @Column(nullable = false)
    private Boolean approved;

    @ManyToOne
    private Staff approvedBy;

    @NotNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private AcademicSession session;

    @OneToOne
    private Event event;

    @ManyToOne
    private Staff appliedStaff;

    @ManyToOne
    private Student appliedStudent;

    @ManyToOne
    private Guardian appliedGuardian;

    @NotNull
    @Column(name = "leave_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate leaveDate;

    @Column
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

    public Staff getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Staff approvedBy) {
        this.approvedBy = approvedBy;
    }

    public AcademicSession getSession() {
        return session;
    }

    public void setSession(AcademicSession session) {
        this.session = session;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Staff getAppliedStaff() {
        return appliedStaff;
    }

    public void setAppliedStaff(Staff appliedStaff) {
        this.appliedStaff = appliedStaff;
    }

    public Student getAppliedStudent() {
        return appliedStudent;
    }

    public void setAppliedStudent(Student appliedStudent) {
        this.appliedStudent = appliedStudent;
    }

    public Guardian getAppliedGuardian() {
        return appliedGuardian;
    }

    public void setAppliedGuardian(Guardian appliedGuardian) {
        this.appliedGuardian = appliedGuardian;
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
        LeaveApplication that = (LeaveApplication) o;
        return Objects.equals(bindingId, that.bindingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindingId);
    }

    @Override
    public String toString() {
        return "LeaveApplication{" +
            "id=" + id +
            ", reason=" + reason +
            ", description='" + description + '\'' +
            ", type=" + type +
            ", approved=" + approved +
            ", approvedBy=" + approvedBy +
            ", session=" + session +
            ", event=" + event +
            ", appliedStaff=" + appliedStaff +
            ", appliedStudent=" + appliedStudent +
            ", appliedGuardian=" + appliedGuardian +
            ", leaveDate=" + leaveDate +
            ", bindingId='" + bindingId + '\'' +
            '}';
    }
}
