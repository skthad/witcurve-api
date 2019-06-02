package com.witcurve.service.impl;

import com.witcurve.config.ApplicationProperties;
import com.witcurve.service.EncryptionService;
import com.witcurve.service.util.AbstractCipherUtil;
import com.witcurve.service.util.Crypt;
import com.witcurve.service.util.EncryptionUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    private final Logger log = LoggerFactory.getLogger(EncryptionServiceImpl.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public String encrypt(String data, String cryptoKey) {

        String result = null;

        if (data == null) return null;

        try {
            if (cryptoKey != null) {
                result = EncryptionUtil.bytesToHex(new Crypt(cryptoKey).encrypt(data));
            } else {
                result = EncryptionUtil.bytesToHex(new Crypt(applicationProperties.getWitcurve().getCryptoKey()).encrypt(data));
            }
        } catch (Exception e) {
            log.error("Exception in encrypt: {}", e, "");
        }

        return result;
    }

    @Override
    public String decrypt(String data, String cryptoKey) {

        String result = null;

        if (data == null) return null;

        try {
            if (cryptoKey != null) {
                result = new Crypt(cryptoKey).decrypt(EncryptionUtil.hexStringToByteArray(data));
            } else {
                result = new Crypt(applicationProperties.getWitcurve().getCryptoKey()).decrypt(EncryptionUtil.hexStringToByteArray(data));
            }
        } catch (Exception e) {
            log.error("Exception in decrypt: {}", e, "");
        }

        return result;
    }

    @Override
    public String encrypt(String data) {

        String cryptoKey = applicationProperties.getWitcurve().getCryptoKey();

        return this.encrypt(data, cryptoKey);
    }

    @Override
    public String decrypt(String data) {

        if (data == null) return null;

        String cryptoKey = applicationProperties.getWitcurve().getCryptoKey();

        return this.decrypt(data, cryptoKey);
    }

    @Override
    public String generateAESKey() {
        try {
            return AbstractCipherUtil.generateAESKey();
        } catch (NoSuchAlgorithmException e) {
            throw new WitcurveException("Unable to generate key");
        }
    }

}
