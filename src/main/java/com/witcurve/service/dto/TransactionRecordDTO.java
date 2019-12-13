package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.witcurve.domain.Attachment;
import com.witcurve.domain.enumeration.ModeOfTransaction;
import com.witcurve.domain.enumeration.RecordType;
import com.witcurve.domain.enumeration.TransactionType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class TransactionRecordDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private RecordType type;

    @NotNull
    private ModeOfTransaction transactionMode;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate transactionDate;

    @NotNull
    private Double totalAmount;

    @NotNull
    private Long schoolInfoId;

    private String description;

    private String transactionId;

    private List<Attachment> attachments;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public RecordType getType() { return type; }

    public void setType(RecordType type) { this.type = type; }

    public ModeOfTransaction getTransactionMode() { return transactionMode; }

    public void setTransactionMode(ModeOfTransaction transactionMode) { this.transactionMode = transactionMode; }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
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

    public List<Attachment> getAttachments() { return attachments; }

    public void setAttachments(List<Attachment> attachments) { this.attachments = attachments; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRecordDTO that = (TransactionRecordDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TransactionRecordDTO{" +
            "id=" + id +
            '}';
    }
}
