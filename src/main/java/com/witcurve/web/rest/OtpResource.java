package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.User;
import com.witcurve.repository.UserRepository;
import com.witcurve.service.MailService;
import com.witcurve.service.OtpService;
import com.witcurve.service.SmsService;
import com.witcurve.web.rest.util.HeaderUtil;
import com.witcurve.web.rest.vm.LoginVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OtpResource {

    private final Logger log = LoggerFactory.getLogger(OtpResource.class);

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
    public ResponseEntity<Void> generateOTP(@RequestBody LoginVM loginVM, @RequestParam(name = "contactNumber") String contactNumber) {
        String username = loginVM.getUsername();
        Optional<User> result = userRepository.findOneByLogin(username);
        String otp;
        if (result.isPresent()) {
            User user = result.get();
            if (user.getOtp() != null && user.getOtpExpiry() != null && user.getOtpExpiry().isAfter(Instant.now())) {
                otp = user.getOtp();
            } else {
                otp = otpService.generateOTP(username);
                user.setOtp(otp);
                user.setOtpExpiry(Instant.now().plusSeconds(300));
                userRepository.save(user);
            }
            smsService.sendSms(contactNumber,String.valueOf(otp));
            if (user.getEmail() != null) {
                // mailService.sendOtpMail(user);
            }
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("OTP sent successfully" + username, username)).build();
    }


    @PostMapping("/validate-otp")
    @Timed
    public ResponseEntity<String> validateOtp(@RequestBody LoginVM loginVM, @RequestParam (name = "otp") String otp) {
        final String OTP_VALID = "Entered OTP is valid";
        final String OTP_INVALID = "Entered OTP is NOT valid. Please Retry!";
        final String OTP_EXPIRED = "Entered OTP has expired. Please regenerate again";
        String username = loginVM.getUsername();
        Optional<User> result = userRepository.findOneByLogin(username);
        if (result.isPresent()) {
            User user = result.get();
            if (user.getOtp() == null || !user.getOtp().equals(otp)) {
                return ResponseEntity.ok().body(OTP_INVALID);
            }
            if (user.getOtpExpiry().isBefore(Instant.now())) {
                return ResponseEntity.ok().body(OTP_EXPIRED);
            }
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
            return ResponseEntity.ok().body(OTP_VALID);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
