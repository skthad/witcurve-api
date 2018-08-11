package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="holiday", uniqueConstraints = {
    @UniqueConstraint(name = "holiday_name_academic_session_UK",
        columnNames = {"name", "academic_session_id"})
})
public class Holiday extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "holidayIdSeq")
    @SequenceGenerator(name = "holidayIdSeq", sequenceName="holiday_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "from_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fromDate;

    @Column(name = "to_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate toDate;

    @Column(name = "holiday_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate holidayDate;

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

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Holiday)) return false;
        Holiday holiday = (Holiday) o;
        return Objects.equals(getId(), holiday.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Holiday{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", fromDate=" + fromDate +
            ", toDate=" + toDate +
            ", holidayDate=" + holidayDate +
            ", academicSession=" + academicSession +
            '}';
    }
}
