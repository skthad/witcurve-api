package com.witcurve.service.util;

import com.witcurve.web.rest.errors.WitcurveException;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;

public class Crypt extends AbstractCipherUtil {
    private final Cipher cipher;

    public Crypt(byte[] key) {
        super(key);
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Crypt(String hexkey) {
        super(hexkey);
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encrypt(String data) throws WitcurveException {
        if (data == null)
            return null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new WitcurveException(String.format("Exception occured while encrypting data: %s", e));
        }
    }

    public String decrypt(byte[] data) throws WitcurveException {
        if (data == null)
            return null;
        try {
            try {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } catch (IllegalStateException il) {
                // try one more time
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            byte[] decrypted = cipher.doFinal(data);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new WitcurveException(String.format("Exception occured while decrypting data: %s", e));
        }
    }
}
