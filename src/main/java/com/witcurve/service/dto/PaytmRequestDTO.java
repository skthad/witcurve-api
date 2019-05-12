package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaytmRequestDTO {

    @NotBlank
    @Size(max = 20)
    @JsonProperty("MID")
    private String merchantMid;

    @NotBlank
    @Size(max = 50)
    @JsonProperty("ORDER_ID")
    private String orderId;

    @NotBlank
    @Size(max = 3)
    @JsonProperty("CHANNEL_ID")
    private String channelId; //WAP, WEB

    @NotBlank
    @Size(max = 64)
    @JsonProperty("CUST_ID")
    private String customerId;

    @Size(max = 15)
    @JsonProperty("MOBILE_NO")
    private String mobileNo;

    @Size(max = 50)
    @JsonProperty("EMAIL")
    private String email;

    @NotBlank
    @Size(max = 10)
    @JsonProperty("TXN_AMOUNT")
    private String transactionAmount;

    @NotBlank
    @Size(max = 30)
    @JsonProperty("WEBSITE")
    private String website; //for staging WEBSTAGING, prod available on activation

    @NotBlank
    @Size(max = 20)
    @JsonProperty("INDUSTRY_TYPE_ID")
    private String industryTypeId; //for staging Retail, ...

    @NotBlank
    @Size(max = 255)
    @JsonProperty("CALLBACK_URL")
    private String callbackUrl;

    @Size(max = 3)
    @JsonProperty("PAYMENT_MODE_ONLY")
    private String paymentModeOnly;

    @Size(max = 10)
    @JsonProperty("AUTH_MODE")
    private String authMode;

    @Size(max = 15)
    @JsonProperty("PAYMENT_TYPE_ID")
    private String paymentTypeId;

    @Size(max = 5)
    @JsonProperty("BANK_CODE")
    private String bankCode;

    @JsonProperty("CHECKSUMHASH")
    private String checkSumHash;

    @JsonProperty("ENVIRONMENT")
    private String environment;

    public String getMerchantMid() {
        return merchantMid;
    }

    public void setMerchantMid(String merchantMid) {
        this.merchantMid = merchantMid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustryTypeId() {
        return industryTypeId;
    }

    public void setIndustryTypeId(String industryTypeId) {
        this.industryTypeId = industryTypeId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getPaymentModeOnly() {
        return paymentModeOnly;
    }

    public void setPaymentModeOnly(String paymentModeOnly) {
        this.paymentModeOnly = paymentModeOnly;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getCheckSumHash() {
        return checkSumHash;
    }

    public void setCheckSumHash(String checkSumHash) {
        this.checkSumHash = checkSumHash;
    }
}
