package com.witcurve.web.rest.vm;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class PaytmStatusCheckVM {
    @NotNull
    private String name;
    @NotNull
    private Double amount;
    @NotNull
    private String itemId;
    @NotNull
    private LocalDate transactionDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "PaytmStatusCheckVM{" +
            "name='" + name + '\'' +
            ", amount=" + amount +
            ", itemId='" + itemId + '\'' +
            ", transactionDate=" + transactionDate +
            '}';
    }
}
