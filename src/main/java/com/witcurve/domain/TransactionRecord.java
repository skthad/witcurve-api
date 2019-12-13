package com.witcurve.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.witcurve.domain.enumeration.ModeOfTransaction;
import com.witcurve.domain.enumeration.RecordType;
import com.witcurve.domain.enumeration.TransactionType;
import com.witcurve.service.util.LocalDateConverter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "transaction_record")
public class TransactionRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecordType type;

    @NotNull
    @Column(name = "transaction_mode", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModeOfTransaction transactionMode;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate transactionDate;

    @NotNull
    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @NotNull
    @Column(nullable = false, name = "total_amount")
    private Double totalAmount;

    @NotNull
    @ManyToOne
    private SchoolInfo schoolInfo;

    @Column
    private String description;

    @Column
    private String transactionId;

    @JsonIgnore
    @OneToMany
    @JoinTable(
        name = "transaction_record_attachments",
        joinColumns = {@JoinColumn(name = "transaction_record_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "attachment_id", referencedColumnName = "id")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Attachment> attachments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecordType getType() {
        return type;
    }

    public void setType(RecordType type) {
        this.type = type;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public ModeOfTransaction getTransactionMode() { return transactionMode; }

    public void setTransactionMode(ModeOfTransaction transactionMode) { this.transactionMode = transactionMode; }

    public void addAttachment(List<Attachment> attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.addAll(attachment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRecord that = (TransactionRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
            "id=" + id +
            '}';
    }
}

