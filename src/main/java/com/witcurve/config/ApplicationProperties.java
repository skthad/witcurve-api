package com.witcurve.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to witcurve.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    public final Sms sms = new Sms();

    public static class Sms {

        private String authKey;

        private String senderId;

        public String getAuthKey() {
            return authKey;
        }

        public void setAuthKey(String authKey) {
            this.authKey = authKey;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

    }

    public Sms getSms() {
        return sms;
    }

    public final Paytm paytm = new Paytm();

    public static class Paytm {

        private String merchantId;

        private String secretKey;

        private String website;

        private String industryTypeId;

        private String callbackUrl;

        private String transactionStatusApi;

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
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

        public String getTransactionStatusApi() {
            return transactionStatusApi;
        }

        public void setTransactionStatusApi(String transactionStatusApi) {
            this.transactionStatusApi = transactionStatusApi;
        }
    }

    public Paytm getPaytm() {
        return paytm;
    }
}
