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
}
