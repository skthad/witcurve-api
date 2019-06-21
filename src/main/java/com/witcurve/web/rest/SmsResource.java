package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Strings;
import com.witcurve.config.Constants;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.OtpPurpose;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.repository.UserRepository;
import com.witcurve.service.MailService;
import com.witcurve.service.OtpService;
import com.witcurve.service.SmsService;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import com.witcurve.web.rest.vm.LoginVM;
import com.witcurve.web.rest.vm.SmsVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SmsResource {

    private final Logger log = LoggerFactory.getLogger(SmsResource.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    OtpService otpService;

    @Autowired
    SmsService smsService;

    @Autowired
    MailService mailService;

    @PostMapping("/generate-otp")
    @Timed
    public ResponseEntity<Void> generateOTP(@RequestBody LoginVM loginVM,
                                            @RequestParam(name = "contactNumber") String contactNumber,
                                            @RequestParam(required = false, defaultValue = "false") Boolean changePassword) throws WitcurveException, UnsupportedEncodingException {
        String username = loginVM.getUsername();
        Optional<User> result = userRepository.findOneByLogin(username);
        String otp;
        String smsSignature = null;
        String instituteName = null;
        String body = "";
        OtpPurpose otpPurpose = changePassword ? OtpPurpose.CHANGE_PASSWORD : OtpPurpose.AUTHENTICATION;
        if (result.isPresent()) {
            User user = result.get();
            int index = user.getLogin().indexOf("-");
            if(index > 0) {
               Long schoolInfoId;
                try {
                    schoolInfoId = Long.parseLong(username.substring(0, index));
                    Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
                    smsSignature = schoolInfo.get().getSchool().getInstitute().getSmsSignature();
                    instituteName = schoolInfo.get().getSchool().getInstitute().getName();
                } catch (NumberFormatException e) {
                    log.error("Entered school info id in user name is wrong : {}", username);
                    throw new WitcurveException("Invalid username, please enter the correct username");
                }
            }
            if (user.getOtp() != null && user.getOtpExpiry() != null && user.getOtpExpiry().isAfter(Instant.now()) && otpPurpose.equals(user.getOtpPurpose())) {
                otp = user.getOtp();
            } else {
                otp = otpService.generateOTP(username);
                user.setOtp(otp);
                user.setOtpPurpose(otpPurpose);
                user.setOtpExpiry(Instant.now().plusSeconds(300));
                userRepository.save(user);
            }
            Map paramsMap = new HashMap();
            paramsMap.put(Constants.PARAM_FULL_NAME, user.getFirstName() + " " + user.getLastName());
            paramsMap.put(Constants.PARAM_OTP, otp);
            paramsMap.put(Constants.PARAM_INSTITUTE_NAME, instituteName);
            if(changePassword) {
                body = String.format("Hello %s, Your OTP for changing password from your mobile application is %s", paramsMap.get(Constants.PARAM_FULL_NAME), paramsMap.get(Constants.PARAM_OTP));
            } else {
                body = String.format("Hello %s, Your OTP for logging in to your mobile application is %s", paramsMap.get(Constants.PARAM_FULL_NAME), paramsMap.get(Constants.PARAM_OTP));
            }
            smsService.sendSms(contactNumber, body, smsSignature);
            if (StringUtils.isNotBlank(user.getEmail())) {
                if (changePassword) {
                    mailService.sendEmailFromTemplate(user.getEmail(), paramsMap,  "mail/changePasswordOtpEmail", "email.pass.otp.title", smsSignature);
                } else {
                    mailService.sendEmailFromTemplate(user.getEmail(), paramsMap,  "mail/authenticationOtpEmail", "email.auth.otp.title", smsSignature);
                }
            }
        } else {
            throw new WitcurveException("User doesn't exist with given login id");
        }

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("OTP sent successfully" + username, username)).build();
    }

    @PostMapping("/send-sms/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<Void> sendBulkSMS(@Valid @RequestBody SmsVM smsVM, @PathVariable Long schoolInfoId) throws WitcurveException, UnsupportedEncodingException {
        log.debug("Request to send bulk SMS for schoolInfoId: " + schoolInfoId);

        if (Strings.isNullOrEmpty(smsVM.getStudentList())
            && Strings.isNullOrEmpty(smsVM.getStaffList())
            && Strings.isNullOrEmpty(smsVM.getGradeList())
            && Strings.isNullOrEmpty(smsVM.getStandardList())) {
            throw new WitcurveException("At least one of studentList, staffList, gradeList or standardList must be provided");
        }
        if(smsVM.getBody() == null) {
            throw new WitcurveException("Sms body is empty");
        }
        smsService.sendBulkSMS(schoolInfoId, smsVM);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("SMS sent successfully", null)).build();
    }

    @PostMapping("/send-sms/intro/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<Void> sendBulkIntroSMS(@Valid @RequestBody SmsVM smsVM, @PathVariable Long schoolInfoId) throws WitcurveException, UnsupportedEncodingException {
        log.debug("Request to send bulk SMS for schoolInfoId: " + schoolInfoId);

        if (Strings.isNullOrEmpty(smsVM.getStudentList())
            && Strings.isNullOrEmpty(smsVM.getStaffList())
            && Strings.isNullOrEmpty(smsVM.getGradeList())
            && Strings.isNullOrEmpty(smsVM.getStandardList())) {
            throw new WitcurveException("At least one of studentList, staffList, gradeList or standardList must be provided");
        }

        smsService.sendBulkIntroSMS(schoolInfoId, smsVM);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("SMS sent successfully", null)).build();
    }
}
