package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="event", uniqueConstraints = {
    @UniqueConstraint(name = "event_name_academic_session_UK",
        columnNames = {"name", "academic_session_id"})
})
public class Event extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventIdSeq")
    @SequenceGenerator(name = "eventIdSeq", sequenceName="event_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "from_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fromDate;

    @Column(name = "to_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate toDate;

    @Column(name = "event_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate eventDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name ="academic_session_id", nullable = false)
    private AcademicSession academicSession;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public AcademicSession getAcademicSession() {
        return academicSession;
    }

    public void setAcademicSession(AcademicSession academicSession) {
        this.academicSession = academicSession;
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", fromDate=" + fromDate +
            ", toDate=" + toDate +
            ", eventDate=" + eventDate +
            ", academicSession=" + academicSession +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
}
