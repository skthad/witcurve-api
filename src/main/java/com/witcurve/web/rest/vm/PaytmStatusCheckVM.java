package com.witcurve.web.rest.vm;

import com.witcurve.domain.enumeration.FeePaymentType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class PaytmStatusCheckVM {
    
    private String name;
    
    private String amount;
    
    private String itemId;
    
    private String transactionDate;
    
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
