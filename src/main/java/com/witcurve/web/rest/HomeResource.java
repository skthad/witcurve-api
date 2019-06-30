package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.HomeService;
import com.witcurve.service.UserService;
import com.witcurve.service.dto.HomeDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class HomeResource {

    private final Logger log = LoggerFactory.getLogger(UserContextResource.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HomeService homeService;

    /**
     * gets the home content for user
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/home/user/{userId}/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<HomeDTO> getCurrentUserContext(@PathVariable Long userId,
                                                         @PathVariable Long schoolInfoId,
                                                         @RequestParam(required = false, defaultValue = "false") Boolean isAdmin) throws WitcurveException {
        log.debug("Request to get current user home content");
        HomeDTO homeDTO;
        if (Boolean.TRUE.equals(isAdmin)) {
            homeDTO = homeService.getCurrentUserHomeContentForAdmin(userId, schoolInfoId);
        } else {
            homeDTO = homeService.getCurrentUserHomeContentForStaff(userId, schoolInfoId);
        }
        return ResponseEntity.ok(homeDTO);
    }
}
