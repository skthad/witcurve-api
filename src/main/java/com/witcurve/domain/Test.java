package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="test", uniqueConstraints = {
    @UniqueConstraint(name = "test_date_time_table_unit_UK",
        columnNames = {"test_date", "time_table_unit_id"})
})
public class Test extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "testIdSeq")
    @SequenceGenerator(name = "testIdSeq", sequenceName="test_id_seq", allocationSize = 0)
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name= "syllabus", nullable = false)
    private String syllabus;

    @NotNull
    @Column(name = "posted_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate postedDate;

    @NotNull
    @Column(name = "test_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate testDate;

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

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
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
        if (!(o instanceof Test)) return false;
        Test test = (Test) o;
        return Objects.equals(getId(), test.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Test{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", syllabus='" + syllabus + '\'' +
            ", postedDate=" + postedDate +
            ", testDate=" + testDate +
            ", timeTableUnit=" + timeTableUnit +
            '}';
    }
}
