package com.witcurve.domain;

import com.witcurve.domain.enumeration.GSDStatus;
import com.witcurve.domain.enumeration.Grade;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="general_slot_details")
public class GeneralSlotDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generalSlotDetailsIdSeq")
    @SequenceGenerator(name = "generalSlotDetailsIdSeq", sequenceName="general_slot_details_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "start_time", nullable = false)
    @Min(0)
    @Max(2359)
    private Integer startTime;

    @NotNull
    @Column(name = "hours", nullable = false)
    @Min(0)
    @Max(23)
    private Integer hours = 0;

    @NotNull
    @Column(name = "minutes", nullable = false)
    @Min(0)
    @Max(59)
    private Integer minutes = 0;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @Column(name = "is_recess", nullable = false)
    private Boolean recess = false;

    @ManyToOne
    @JoinColumn(name = "standard_id")
    private Standard standard;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @NotNull
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private GSDStatus status;

    @Column
    private String bindingId;

    @ManyToOne
    @JoinColumn
    private Exam exam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getRecess() {
        return recess;
    }

    public void setRecess(Boolean recess) {
        this.recess = recess;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public GSDStatus getStatus() {
        return status;
    }

    public void setStatus(GSDStatus status) {
        this.status = status;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneralSlotDetails)) return false;
        GeneralSlotDetails that = (GeneralSlotDetails) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "GeneralSlotDetails{" +
            "id=" + id +
            ", startTime='" + startTime + '\'' +
            ", duration=" + duration +
            ", recess=" + recess +
            (exam == null ? (", standard=" + standard) : "") +
            (exam != null ? (", grade=" + grade) : "") +
            (exam != null ? (", exam=" + exam) : "") +
            '}';
    }
}
