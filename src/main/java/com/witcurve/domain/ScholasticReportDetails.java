package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "scholastic_report_details")
public class ScholasticReportDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean showGrades = false;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean showMarks = false;

    @NotNull
    @Column(nullable = false)
    private Double marksNormalisation;

    @Column
    private String header;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private ReportCardDesign reportCardDesign;

    @ManyToOne
    private ReportCard reportCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getShowGrades() {
        return showGrades;
    }

    public void setShowGrades(Boolean showGrades) {
        this.showGrades = showGrades;
    }

    public Boolean getShowMarks() {
        return showMarks;
    }

    public void setShowMarks(Boolean showMarks) {
        this.showMarks = showMarks;
    }

    public Double getMarksNormalisation() {
        return marksNormalisation;
    }

    public void setMarksNormalisation(Double marksNormalisation) {
        this.marksNormalisation = marksNormalisation;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ReportCardDesign getReportCardDesign() {
        return reportCardDesign;
    }

    public void setReportCardDesign(ReportCardDesign reportCardDesign) {
        this.reportCardDesign = reportCardDesign;
    }

    public ReportCard getReportCard() {
        return reportCard;
    }

    public void setReportCard(ReportCard reportCard) {
        this.reportCard = reportCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScholasticReportDetails that = (ScholasticReportDetails) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ScholasticReportDetails{" +
            "id=" + id +
            '}';
    }
}
