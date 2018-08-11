package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="slot")
public class Slot extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slotIdSeq")
    @SequenceGenerator(name = "slotIdSeq", sequenceName="slot_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private String startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private String endTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class standard;

    @NotNull
    @Column(name="position", nullable = false)
    private Integer position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Class getStandard() {
        return standard;
    }

    public void setStandard(Class standard) {
        this.standard = standard;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(id, slot.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Slot{" +
            "id=" + id +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", standard=" + standard +
            ", position=" + position +
            '}';
    }
}
