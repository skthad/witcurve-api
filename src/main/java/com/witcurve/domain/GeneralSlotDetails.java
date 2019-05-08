package com.witcurve.domain;

import com.witcurve.domain.enumeration.GSDStatus;
import com.witcurve.domain.enumeration.Grade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="general_slot_details")
public class GeneralSlotDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String bindingId;

    @NotNull
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private GSDStatus status;

    @ManyToOne
    @JoinColumn(name = "standard_id")
    private Standard standard;

    @Column
    private Grade grade;

    @NotNull
    @Column(name = "start", nullable = false)
    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String start;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @Column(name = "is_recess", nullable = false, columnDefinition = "boolean default false")
    private Boolean recess = false;

    @ManyToOne
    @JoinColumn
    private Exam exam;

    @Column
    private Integer slotOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
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

    public Integer getSlotOrder() {
        return slotOrder;
    }

    public void setSlotOrder(Integer slotOrder) {
        this.slotOrder = slotOrder;
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
            '}';
    }
}
