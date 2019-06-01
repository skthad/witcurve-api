package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Strings;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.OtpPurpose;
import com.witcurve.repository.UserRepository;
import com.witcurve.service.MailService;
import com.witcurve.service.OtpService;
import com.witcurve.service.SmsService;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import com.witcurve.web.rest.vm.LoginVM;
import com.witcurve.web.rest.vm.SmsVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SmsResource {

    private final Logger log = LoggerFactory.getLogger(SmsResource.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    OtpService otpService;

    @Autowired
    SmsService smsService;

    @Autowired
    MailService mailService;

    @PostMapping("/generate-otp")
    @Timed
    public ResponseEntity<Void> generateOTP(@RequestBody LoginVM loginVM, @RequestParam(name = "contactNumber") String contactNumber, @RequestParam(required = false, defaultValue = "false") Boolean changePassword) throws WitcurveException, UnsupportedEncodingException {
        String username = loginVM.getUsername();
        Optional<User> result = userRepository.findOneByLogin(username);
        String otp;
        OtpPurpose otpPurpose = changePassword ? OtpPurpose.CHANGE_PASSWORD : OtpPurpose.AUTHENTICATION;
        if (result.isPresent()) {
            User user = result.get();
            if (user.getOtp() != null && user.getOtpExpiry() != null && user.getOtpExpiry().isAfter(Instant.now()) && otpPurpose.equals(user.getOtpPurpose())) {
                otp = user.getOtp();
            } else {
                otp = otpService.generateOTP(username);
                user.setOtp(otp);
                user.setOtpPurpose(otpPurpose);
                user.setOtpExpiry(Instant.now().plusSeconds(300));
                userRepository.save(user);
            }
            if(changePassword) {
                smsService.sendSms(contactNumber,"Hello User, Your OTP for changing password from your mobile application is " + otp);
            } else {
                smsService.sendSms(contactNumber,"Hello User, Your OTP for logging in to your mobile application is " + otp);
            }

            if (user.getEmail() != null) {
                //TODO create otp mail for logging in and for changing password
                mailService.sendOtpMail(user);
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

        smsService.sendBulkSMS(schoolInfoId, smsVM);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("SMS sent successfully", null)).build();
    }
}
