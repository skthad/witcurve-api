package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaytmResponseDTO {

    @JsonProperty("MID")
    private String merchantId;

    @JsonProperty("TXNID")
    private String transactionId;

    @JsonProperty("ORDERID")
    private String orderId;

    @JsonProperty("CUST_ID")
    private String customerId;

    @JsonProperty("BANKTXNID")
    private String bankTransactionId;

    @JsonProperty("TXNAMOUNT")
    private String transactionAmount;

    @JsonProperty("CURRENCY")
    private String currency;

    @JsonProperty("STATUS")
    private String status; //TXN_SUCCESS, TXN_FAILURE and PENDING

    //https://developer.paytm.com/assets/Transaction%20response%20codes%20and%20messages.pdf

    @JsonProperty("RESPCODE")
    private String responseCode;

    @JsonProperty("RESPMSG")
    private String responseMessage;

    @JsonProperty("TXNDATE")
    private String transactionDate;

    @JsonProperty("GATEWAYNAME")
    private String gatewayName;

    @JsonProperty("BANKNAME")
    private String bankName;

    @JsonProperty("PAYMENTMODE")
    private String paymentMode; // CC, DC, NB, UPI, PPI

    @JsonProperty("CHECKSUMHASH")
    private String checkSumHash;

    @JsonProperty("BIN_NUMBER")
    private String binNumber;

    @JsonProperty("CARD_LAST_NUMS")
    private String cardLastNums;

    //below fields are included verification response

    @JsonProperty("TXNTYPE")
    private String transactionType;

    @JsonProperty("REFUNDAMT")
    private String refundAmount;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBankTransactionId() {
        return bankTransactionId;
    }

    public void setBankTransactionId(String bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getCheckSumHash() {
        return checkSumHash;
    }

    public void setCheckSumHash(String checkSumHash) {
        this.checkSumHash = checkSumHash;
    }

    public String getBinNumber() {
        return binNumber;
    }

    public void setBinNumber(String binNumber) {
        this.binNumber = binNumber;
    }

    public String getCardLastNums() {
        return cardLastNums;
    }

    public void setCardLastNums(String cardLastNums) {
        this.cardLastNums = cardLastNums;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    @Override
    public String toString() {
        return "PaytmResponseDTO{" +
            "merchantId='" + merchantId + '\'' +
            ", transactionId='" + transactionId + '\'' +
            ", orderId='" + orderId + '\'' +
            ", customerId='" + customerId + '\'' +
            ", bankTransactionId='" + bankTransactionId + '\'' +
            ", transactionAmount='" + transactionAmount + '\'' +
            ", currency='" + currency + '\'' +
            ", status='" + status + '\'' +
            ", responseCode='" + responseCode + '\'' +
            ", responseMessage='" + responseMessage + '\'' +
            ", transactionDate='" + transactionDate + '\'' +
            ", gatewayName='" + gatewayName + '\'' +
            ", bankName='" + bankName + '\'' +
            ", paymentMode='" + paymentMode + '\'' +
            ", checkSumHash='" + checkSumHash + '\'' +
            ", binNumber='" + binNumber + '\'' +
            ", cardLastNums='" + cardLastNums + '\'' +
            ", transactionType='" + transactionType + '\'' +
            ", refundAmount='" + refundAmount + '\'' +
            '}';
    }
}
