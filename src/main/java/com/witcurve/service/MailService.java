package com.witcurve.service;

import com.witcurve.config.Constants;
import com.witcurve.domain.User;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully to '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    public void sendEmailFromTemplate(String to, Map<String, Object> params, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(Constants.DEFAULT_LANGUAGE);
        Context context = new Context(locale);
        context.setVariables(params);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(to, subject, content, false, true);

    }


    @Async
    public void sendAuthenticationOtpMail(User user) {
        log.debug("Sending Authentication OTP email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(), Collections.singletonMap(USER, user), "mail/authenticationOtpEmail", "email.auth.otp.title");
    }


    @Async
    public void sendChangePasswordOtpMail(User user) {
        log.debug("Sending Change Password OTP email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(), Collections.singletonMap(USER, user), "mail/changePasswordOtpEmail", "email.pass.otp.title");
    }

    @Async
    public void sendSubscriptionTransactionMail(String to, Map<String, Object> params) {
        log.debug("Sending Subscription Transaction email to '{}'", to);
        sendEmailFromTemplate(to, params, "mail/subscriptionTransactionEmail", "email.subscription.transaction.title");
    }

    @Async
    public void sendResetPasswordMail(String to, Map<String, Object> params) {
        log.debug("Sending Reset Password email to '{}'", to);
        sendEmailFromTemplate(to, params, "mail/resetPasswordEmail", "email.reset.password.title");
    }

    @Async
    public void sendSubscriptionTransactionMail(User user) {
        log.debug("Sending Subscription Transaction email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(), Collections.singletonMap(USER, user), "mail/subscriptionTransactionEmail", "email.subscription.transaction.title");
    }

    @Async
    public void sendLeaveApplicationMail(User user) {
        log.debug("Sending Leave Application email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(), Collections.singletonMap(USER, user), "mail/leaveApplicationEmail", "email.leave.application.title");
    }

    @Async
    public void sendMeetingRequestMail(User user) {
        log.debug("Sending Meeting Request email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(), Collections.singletonMap(USER, user), "mail/meetingRequestEmail", "email.meeting.request.title");
    }

    @Async
    public void sendPersonalMessageMail(User user) {
        log.debug("Sending Personal Message email to '{}'", user.getEmail());
        sendEmailFromTemplate(user.getEmail(), Collections.singletonMap(USER, user), "mail/personalMessageEmail", "email.personal.message.title");
    }

}
