package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.MailService;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.EmailVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api")
public class MailResource {

    private final Logger log = LoggerFactory.getLogger(MailResource.class);

    @Autowired
    MailService mailService;

    @PostMapping("/send-mail/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<Void> sendBulkEmail(@Valid @RequestBody EmailVM emailVM, @PathVariable Long schoolInfoId) throws WitcurveException, UnsupportedEncodingException {
        log.debug("Send bulk email for school info with id : {} for {}", schoolInfoId, emailVM);
        if(emailVM.getSubject() == null || emailVM.getBody() == null) {
            throw new WitcurveException("Please make sure body and subject fields are not empty");
        }
        mailService.sendBulkEmail(emailVM, schoolInfoId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/send-mail/intro/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<Void> sendIntroBulkEmail(@Valid @RequestBody EmailVM emailVM, @PathVariable Long schoolInfoId) throws WitcurveException, UnsupportedEncodingException {
        log.debug("Send bulk email for school info with id : {} for {}", schoolInfoId, emailVM);
        mailService.sendIntoBulkEmail(emailVM, schoolInfoId);
        return ResponseEntity.ok(null);
    }
}
