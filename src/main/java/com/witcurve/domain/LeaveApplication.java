package com.witcurve.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.Reason;
import com.witcurve.service.util.LocalDateConverter;

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
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @ManyToOne
    private Staff approvedBy;

    @NotNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private SchoolInfo schoolInfo;

    @ManyToOne
    private Staff appliedStaff;

    @ManyToOne
    private Student appliedStudent;

    @NotNull
    @Column(name = "from_leave_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fromLeaveDate;

    @NotNull
    @Column(name = "to_leave_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate toLeaveDate;

    @Column
    private String note;

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

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public Staff getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Staff approvedBy) {
        this.approvedBy = approvedBy;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
            ", status=" + status +
            ", approvedBy=" + approvedBy +
            ", schoolInfo=" + schoolInfo +
            ", appliedStaff=" + appliedStaff +
            ", appliedStudent=" + appliedStudent +
            ", fromLeaveDate=" + fromLeaveDate +
            ", toLeaveDate=" + toLeaveDate +
            ", note='" + note + '\'' +
            ", events=" + events +
            '}';
    }
}
