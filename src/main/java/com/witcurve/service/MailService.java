package com.witcurve.service;

import com.google.common.base.Strings;
import com.witcurve.config.Constants;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.repository.StaffRepository;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.EmailVM;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String PARAMS_MAP = "paramsMap";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final StudentStandardRepository studentStandardRepository;

    private final StudentRepository studentRepository;

    private final StaffRepository staffRepository;

    private final SchoolInfoRepository schoolInfoRepository;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine, StudentRepository studentRepository,
                       StaffRepository staffRepository, StudentStandardRepository studentStandardRepository, SchoolInfoRepository schoolInfoRepository) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.staffRepository = staffRepository;
        this.studentRepository = studentRepository;
        this.studentStandardRepository = studentStandardRepository;
        this.schoolInfoRepository = schoolInfoRepository;
    }

    @Async
    public void sendEmail(String to, String from, String subject, String content, boolean isMultipart, boolean isHtml) {
        String[] toEmails = new String[1];
        toEmails[0] = to;
        sendEmail(toEmails, from, subject, content, isMultipart, isHtml);
    }

    @Async
    public void sendEmail(String[] to, String from, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(from);
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
    public void sendBulkEmail(EmailVM emailVM, Long schoolInfoId)  throws WitcurveException {
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No SchoolInfo with given id " + schoolInfoId);
        }
        String fromEmail = schoolInfo.get().getSchool().getInstitute().getSmsSignature().toLowerCase() + "@witcurve.com";
        Set<String> recipientsList = new HashSet<>();
        if (!Strings.isNullOrEmpty(emailVM.getStudentList())) {
            if (emailVM.getStudentList().equals("-1")) {
                recipientsList.addAll(studentStandardRepository.getActiveStudentEmailsBySchoolInfoId(schoolInfoId));
            } else {
                List<Long> studentIds = Arrays.asList(emailVM.getStudentList().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                recipientsList.addAll(studentRepository.getEmailsBySchoolInfoAndStudentIds(schoolInfoId, studentIds));
            }
        }
        if (!Strings.isNullOrEmpty(emailVM.getStaffList())) {
            if (emailVM.getStaffList().equals("-1")) {
                recipientsList.addAll(staffRepository.findActiveStaffEmailsInSchoolInfoId(schoolInfoId));
            } else {
                List<Long> staffIds = Arrays.asList(emailVM.getStaffList().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                recipientsList.addAll(staffRepository.getEmailsBySchoolInfoAndStaffIds(schoolInfoId, staffIds));
            }
        }
        if (!Strings.isNullOrEmpty(emailVM.getStandardList())) {
            List<Long> standardIds = Arrays.asList(emailVM.getStandardList().split(","))
                .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            recipientsList.addAll(studentStandardRepository.getActiveStudentEmailsBySchoolInfoIdAndStandardIds(schoolInfoId, standardIds));
        }
        if (!Strings.isNullOrEmpty(emailVM.getGradeList())) {
            List<Grade> gradeList;
            try {
                gradeList = Arrays.asList(emailVM.getGradeList().split(","))
                    .stream().map(s -> Grade.valueOf(s.trim())).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new WitcurveException("Invalid grade value");
            }

            recipientsList.addAll(studentStandardRepository.getActiveStudentEmailsBySchoolInfoIdAndGradeList(schoolInfoId, gradeList));
        }
        if(recipientsList.size()!=0) {
            String[] to = recipientsList.toArray(new String[0]);
            sendEmail(to, fromEmail, emailVM.getSubject(), emailVM.getBody(), false, false);
        } else {
            throw new WitcurveException("There are no email records available for given recipient list");
        }

    }

    @Async
    public void sendEmailFromTemplate(String toEmail, Map<String, Object> singleMap, String templateName, String titleKey, String smsSignature) {
        HashMap<String, Map<String, Object>> paramsMap = new HashMap<>();
        paramsMap.put(toEmail, singleMap);
        sendEmailFromTemplate(paramsMap, templateName, titleKey, smsSignature);
    }

    @Async
    public void sendEmailFromTemplate(Map<String, Map<String, Object>> emailIdParamsMap, String templateName, String titleKey, String smsSignature) {
        String fromEmail = (Strings.isNullOrEmpty(smsSignature) ? "admin" : smsSignature.toLowerCase()) + "@witcurve.com";
        for (String toEmail : emailIdParamsMap.keySet()) {
            log.debug("Sending mail for template with name : {} and title Key : {} to email : {}", templateName, titleKey, toEmail);
            Locale locale = Locale.forLanguageTag(Constants.DEFAULT_LANGUAGE);
            Context context = new Context(locale);
            if (emailIdParamsMap.get(toEmail) != null) {
                context.setVariables(emailIdParamsMap.get(toEmail));
            }
            context.setVariable(Constants.PARAM_SMS_SIGNATURE, smsSignature);
            context.setVariable(Constants.PARAM_BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            String content = templateEngine.process(templateName, context);
            String subject = messageSource.getMessage(titleKey, (String[])emailIdParamsMap.get(toEmail).get(Constants.PARAM_MAIL_SUBJECT), locale);
            sendEmail(toEmail, fromEmail, subject, content, false, true);
        }
    }

}
