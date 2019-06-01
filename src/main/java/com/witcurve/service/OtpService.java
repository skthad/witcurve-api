package com.witcurve.service;

import com.witcurve.service.util.OtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    private static final Logger log = LoggerFactory.getLogger("OtpService");

    private static final Integer OTP_SIZE = 4;

    public String generateOTP(String key) {
        String otp = null;
        try {
            otp = OtpUtil.generate(OTP_SIZE);
        } catch (Exception e) {
            log.error("Failure while generating otp "+e.getMessage());
        }
        return otp;
    }
}
