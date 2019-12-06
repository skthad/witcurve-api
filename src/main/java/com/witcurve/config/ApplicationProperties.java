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
    public final Aws aws = new Aws();
    public final Paytm paytm = new Paytm();
    public final Witcurve witcurve = new Witcurve();


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

    public static class Paytm {

        private String merchantId;

        private String secretKey;

        private String website;

        private String industryTypeId;

        private String callbackUrl;

        private String transactionStatusApi;

        private String communicationIps;

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

        public String getCommunicationIps() { return communicationIps; }

        public void setCommunicationIps(String communicationIps) { this.communicationIps = communicationIps; }

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

    public static class Witcurve {
        private String cryptoKey;

        private String domainUrl;

        public String getCryptoKey() {
            return cryptoKey;
        }

        public void setCryptoKey(String cryptoKey) {
            this.cryptoKey = cryptoKey;
        }

        public String getDomainUrl() {
            return domainUrl;
        }

        public void setDomainUrl(String domainUrl) {
            this.domainUrl = domainUrl;
        }
    }

    public Witcurve getWitcurve() {
        return witcurve;
    }

    public static class Aws {

        private String accesskey;

        private String secretKey;

        private String defaultRegion;

        private String snsRegion;

        private String snsApplicationArn;

        private String bucketName;

        public String getAccesskey() {
            return accesskey;
        }

        public void setAccesskey(String accesskey) {
            this.accesskey = accesskey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getDefaultRegion() {
            return defaultRegion;
        }

        public void setDefaultRegion(String defaultRegion) {
            this.defaultRegion = defaultRegion;
        }

        public String getSnsRegion() {
            return snsRegion;
        }

        public void setSnsRegion(String snsRegion) {
            this.snsRegion = snsRegion;
        }

        public String getSnsApplicationArn() {
            return snsApplicationArn;
        }

        public void setSnsApplicationArn(String snsApplicationArn) {
            this.snsApplicationArn = snsApplicationArn;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }
    }

    public Aws getAws() {
        return aws;
    }
}
