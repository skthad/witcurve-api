package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PaytmVerificationRequestDTO {

    @NotBlank
    @Size(max = 20)
    @JsonProperty("MID")
    private String merchantId;

    @NotBlank
    @Size(max = 50)
    @JsonProperty("ORDERID")
    private String orderId;

    @NotBlank
    @Size(max = 108)
    @JsonProperty("CHECKSUMHASH")
    private String checkumHash;

    @JsonProperty("TXN_TYPE")
    private String transactionType;  //PREAUTH/RELEASE/CAPTURE/WITHDRAW, type was alpha

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCheckumHash() {
        return checkumHash;
    }

    public void setCheckumHash(String checkumHash) {
        this.checkumHash = checkumHash;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
