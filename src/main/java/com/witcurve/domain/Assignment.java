package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="assignment")
public class Assignment extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assignmentIdSeq")
    @SequenceGenerator(name = "assignmentIdSeq", sequenceName="assignment_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "posted_date", nullable = false)
    private LocalDate postedDate;

    @NotNull
    @Column(name = "submission_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate submissionDate;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private TimeTableUnit timeTableUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public TimeTableUnit getTimeTableUnit() {
        return timeTableUnit;
    }

    public void setTimeTableUnit(TimeTableUnit timeTableUnit) {
        this.timeTableUnit = timeTableUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assignment)) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Assignment{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", postedDate=" + postedDate +
            ", submissionDate=" + submissionDate +
            ", timeTableUnit=" + timeTableUnit +
            '}';
    }
}
