package com.witcurve.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gag.annotation.remark.OhNoYouDidnt;
import com.witcurve.domain.enumeration.LeaveApplyor;
import com.witcurve.domain.enumeration.Reason;
import com.witcurve.service.util.LocalDateConverter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @ManyToOne
    private Staff appliedStaff;

    @ManyToOne
    private Student appliedStudent;

    @ManyToOne
    private Guardian appliedGuardian;

    @NotNull
    @Column(name = "from_leave_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fromLeaveDate;

    @NotNull
    @Column(name = "to_leave_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate toLeaveDate;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "event_leave_application",
        joinColumns = {@JoinColumn(name = "leave_application_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")})
    private Set<Event> events = new HashSet<>();

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

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void addEvents(Event e) {
        if(this.events == null) {
            this.events = new HashSet();
        }
        this.events.add(e);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaveApplication that = (LeaveApplication) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
            ", appliedStaff=" + appliedStaff +
            ", appliedStudent=" + appliedStudent +
            ", appliedGuardian=" + appliedGuardian +
            ", fromLeaveDate=" + fromLeaveDate +
            ", toLeaveDate=" + toLeaveDate +
            '}';
    }
}
