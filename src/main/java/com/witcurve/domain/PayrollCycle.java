package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

@Entity
@Table(name="payroll_cycle", uniqueConstraints = {
    @UniqueConstraint(name = "payroll_cycle_school_info_month_year_UK",
        columnNames = {"school_info_id", "month", "year"})
})
public class PayrollCycle extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payrollCycleIdSeq")
    @SequenceGenerator(name = "payrollCycleIdSeq", sequenceName="payroll_cycle_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SchoolInfo schoolInfo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "month", nullable = false)
    private Month month;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2099)
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate cycleStart;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate cycleEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getCycleStart() {
        return cycleStart;
    }

    public void setCycleStart(LocalDate cycleStart) {
        this.cycleStart = cycleStart;
    }

    public LocalDate getCycleEnd() {
        return cycleEnd;
    }

    public void setCycleEnd(LocalDate cycleEnd) {
        this.cycleEnd = cycleEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PayrollCycle)) return false;
        PayrollCycle that = (PayrollCycle) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "PayrollCycle{" +
            "id=" + id +
            '}';
    }
}
