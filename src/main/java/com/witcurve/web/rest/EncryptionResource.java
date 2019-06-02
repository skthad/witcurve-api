package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.EncryptionService;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class EncryptionResource {

    @Autowired
    private EncryptionService encryptionService;

    /**
     *
     * @param data string
     * @param encrypt boolean
     * @return the ResponseEntity with status 200 (OK) and the encrypted or decrypted value
     * @throws com.witcurve.web.rest.errors.WitcurveException
     */

    @PostMapping("/encrypt-decrypt")
    @Timed
    public ResponseEntity<String> encryptOrDecrypt(@RequestBody String data,
                                                   @RequestParam(required = false) Boolean encrypt,
                                                   @RequestParam(required = false) String cryptoKey) throws WitcurveException {
        String response;
        if (Boolean.TRUE.equals(encrypt)) {
            response = encryptionService.encrypt(data, cryptoKey);
        } else {
            response = encryptionService.decrypt(data, cryptoKey);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/encrypt-decrypt/key")
    @Timed
    public ResponseEntity<String> generateAESKey() throws WitcurveException {
        String response = encryptionService.generateAESKey();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
