package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="daily_update", uniqueConstraints = {
    @UniqueConstraint(name = "posted_date_time_table_unit_UK",
        columnNames = {"posted_date", "time_table_unit_id"})
})
public class DailyUpdate extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dailyUpdateIdSeq")
    @SequenceGenerator(name = "dailyUpdateIdSeq", sequenceName="daily_update_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "posted_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate postedDate;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

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

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof DailyUpdate)) return false;
        DailyUpdate that = (DailyUpdate) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "DailyUpdate{" +
            "id=" + id +
            ", postedDate=" + postedDate +
            ", description='" + description + '\'' +
            ", timeTableUnit=" + timeTableUnit +
            '}';
    }
}
