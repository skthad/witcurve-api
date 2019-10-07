package com.witcurve.domain;

import com.witcurve.domain.enumeration.ReportStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "standard",   uniqueConstraints = {
    @UniqueConstraint(name = "standard_report_card_UK",
        columnNames = {"standard_id", "report_card_id"})
})
public class StandardReport extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private ReportCard reportCard;

    @NotNull
    @ManyToOne
    @JoinColumn( nullable = false)
    private Standard standard;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @NotNull
    @OneToOne
    @JoinColumn(nullable = false)
    private Attachment withHeader;

    @NotNull
    @OneToOne
    @JoinColumn(nullable = false)
    private Attachment withOutHeader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportCard getReportCard() {
        return reportCard;
    }

    public void setReportCard(ReportCard reportCard) {
        this.reportCard = reportCard;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public Attachment getWithHeader() {
        return withHeader;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public void setWithHeader(Attachment withHeader) {
        this.withHeader = withHeader;
    }

    public Attachment getWithOutHeader() {
        return withOutHeader;
    }

    public void setWithOutHeader(Attachment withOutHeader) {
        this.withOutHeader = withOutHeader;
    }

    @Override
    public String toString() {
        return "StandardReport{" +
            "id=" + id +
            '}';
    }
}
