package com.witcurve.domain;

import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.service.util.LocalDateConverter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="exam")
public class Exam extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name ="school_info_id", nullable = false)
    private SchoolInfo schoolInfo;

    @NotNull
    @Column(name = "status", nullable = false, length = 50, columnDefinition = "varchar(50) default 'DRAFT'")
    @Enumerated(EnumType.STRING)
    private ExamStatus status = ExamStatus.DRAFT;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "start_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate endDate;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="exam_id", insertable = false, updatable = false)
    @Where(clause = "status='ACTIVE'")
    private Set<GeneralSlotDetails> generalSlotDetails;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="exam_id", insertable = false, updatable = false)
    @Where(clause = "field_type='MAIN' and selected = true")
    private Set<ReportCardDesign> mainReportCardDesigns;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="exam_id", insertable = false, updatable = false)
    @Where(clause = "field_type='ATTRIBUTES'")
    private Set<GeneralSlotDetails> attributeReportCardDesigns;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="exam_id", insertable = false, updatable = false)
    @Where(clause = "field_type='REMARKS'")
    private Set<GeneralSlotDetails> remarkReportCardDesigns;



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

    public ExamStatus getStatus() {
        return status;
    }

    public void setStatus(ExamStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<GeneralSlotDetails> getGeneralSlotDetails() {
        return generalSlotDetails;
    }

    public void setGeneralSlotDetails(Set<GeneralSlotDetails> generalSlotDetails) {
        this.generalSlotDetails = generalSlotDetails;
    }

    public Set<ReportCardDesign> getMainReportCardDesigns() {
        return mainReportCardDesigns;
    }

    public void setMainReportCardDesigns(Set<ReportCardDesign> mainReportCardDesigns) {
        this.mainReportCardDesigns = mainReportCardDesigns;
    }

    public Set<GeneralSlotDetails> getAttributeReportCardDesigns() {
        return attributeReportCardDesigns;
    }

    public void setAttributeReportCardDesigns(Set<GeneralSlotDetails> attributeReportCardDesigns) {
        this.attributeReportCardDesigns = attributeReportCardDesigns;
    }

    public Set<GeneralSlotDetails> getRemarkReportCardDesigns() {
        return remarkReportCardDesigns;
    }

    public void setRemarkReportCardDesigns(Set<GeneralSlotDetails> remarkReportCardDesigns) {
        this.remarkReportCardDesigns = remarkReportCardDesigns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;
        Exam exam = (Exam) o;
        return Objects.equals(getId(), exam.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Exam{" +
            "id=" + id +
            '}';
    }
}
