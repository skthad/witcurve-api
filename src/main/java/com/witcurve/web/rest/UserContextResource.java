package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.UserContextService;
import com.witcurve.service.dto.UserContextDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class UserContextResource {

    private final Logger log = LoggerFactory.getLogger(UserContextResource.class);

    @Autowired
    UserContextService userContextService;

    /**
     * gets the current user context
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/user-context")
    @Timed
    public ResponseEntity<UserContextDTO> getCurrentUserContext() throws WitcurveException {
        log.debug("Request to get current user context");
        UserContextDTO result = userContextService.getCurrentUserContext();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
