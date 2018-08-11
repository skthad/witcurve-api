package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="academic_session", uniqueConstraints = {
    @UniqueConstraint(name = "start_date_school_id_UK",
        columnNames = {"school_id", "start_date"})
})
public class AcademicSession extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "academicSessionIdSeq")
    @SequenceGenerator(name = "academicSessionIdSeq", sequenceName="academic_session_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private School school;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcademicSession that = (AcademicSession) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AcademicSession{" +
            "id=" + id +
            ", startDate=" + startDate +
            ", school=" + school +
            '}';
    }
}
