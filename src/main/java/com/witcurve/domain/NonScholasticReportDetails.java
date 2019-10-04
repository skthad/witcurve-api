package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "non_scholastic_report_details")
public class NonScholasticReportDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String header;

    @NotNull
    @Column(nullable = false)
    private Integer nonScholasticOrder;

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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Integer getNonScholasticOrder() {
        return nonScholasticOrder;
    }

    public void setNonScholasticOrder(Integer nonScholasticOrder) {
        this.nonScholasticOrder = nonScholasticOrder;
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
        NonScholasticReportDetails that = (NonScholasticReportDetails) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "NonScholasticReportDetails{" +
            "id=" + id +
            '}';
    }
}
