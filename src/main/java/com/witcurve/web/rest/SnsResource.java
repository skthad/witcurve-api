package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SnsService;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.PublishMessageVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api")
public class SnsResource {

    private final Logger log = LoggerFactory.getLogger(SnsResource.class);

    @Autowired
    SnsService snsService;

    @GetMapping("/test-sns")
    @Timed
    public ResponseEntity<Void> sendTestSns() throws UnsupportedEncodingException {
        snsService.sendMobilePushNotification();
        return ResponseEntity.ok(null);
    }

    @PostMapping("/sns/publish-message")
    @Timed
    public ResponseEntity<Void> publishMessage(@RequestBody PublishMessageVM publishMessageVM) throws WitcurveException {
        log.debug("Publish message : {}",publishMessageVM);
        if(publishMessageVM.getEndPoint() == null) {
            throw new WitcurveException("End point is required");
        }
        snsService.publishMessage(publishMessageVM.getMessage(), publishMessageVM.getUrl(), publishMessageVM.getEndPoint());
        return ResponseEntity.ok(null);
    }

    @PostMapping("/sns/publish-bulk-message")
    @Timed
    public ResponseEntity<Void> publishBulkMessages(@RequestBody PublishMessageVM publishMessageVM) throws WitcurveException {
        log.debug("Bulk publish message : {}", publishMessageVM);
        if(publishMessageVM.getType() == null) {
            throw new WitcurveException("Field type is required");
        }
        snsService.publishBulkMessage(publishMessageVM.getMessage(), publishMessageVM.getUrl(), publishMessageVM.getType(), publishMessageVM.getSchoolInfoId());
        return ResponseEntity.ok(null);
    }
}
