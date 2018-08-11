package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="term", uniqueConstraints = {
    @UniqueConstraint(name = "academic_session_start_date_UK",
        columnNames = {"start_date", "session_id"})
})
public class Term extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "termIdSeq")
    @SequenceGenerator(name = "termIdSeq", sequenceName="term_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false, name = "session_id")
    private AcademicSession session;

    @NotNull
    @Column(nullable = false, name = "start_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate startDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AcademicSession getSession() {
        return session;
    }

    public void setSession(AcademicSession session) {
        this.session = session;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(id, term.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Term{" +
            "id=" + id +
            ", session=" + session +
            ", startDate=" + startDate +
            '}';
    }
}
