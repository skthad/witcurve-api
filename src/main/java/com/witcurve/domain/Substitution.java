package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="substitution", uniqueConstraints = {
    @UniqueConstraint(name = "scd_date_id_UK",
        columnNames = {"scd_id", "date"})
})
public class Substitution extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "substitutionIdSeq")
    @SequenceGenerator(name = "substitutionIdSeq", sequenceName="substitution_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Staff teacher;

    @ManyToOne
    @JoinColumn(name = "scd_id", nullable = false)
    private SlotCourseDetails scd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Staff getTeacher() {
        return teacher;
    }

    public void setTeacher(Staff teacher) {
        this.teacher = teacher;
    }

    public SlotCourseDetails getScd() {
        return scd;
    }

    public void setScd(SlotCourseDetails scd) {
        this.scd = scd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Substitution that = (Substitution) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Subsitution{" +
            "id=" + id +
            '}';
    }
}
