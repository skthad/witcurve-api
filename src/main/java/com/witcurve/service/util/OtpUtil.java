package com.witcurve.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Anuranjan on 8/18/2018.
 */
public class OtpUtil {
    private static final Logger logger = LoggerFactory.getLogger("OtpUtil");

    public static String generate(int size) {

        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstanceStrong();

            for (int i = 0; i < size; i++) {
                generatedToken.append(number.nextInt(9));
            }

        } catch (NoSuchAlgorithmException e) {
            logger.error("Error while generating OTP: "+e.getMessage());
        }

        return generatedToken.toString();
    }

}
