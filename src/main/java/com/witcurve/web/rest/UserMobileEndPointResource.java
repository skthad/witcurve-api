package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.security.PermissionsConstants;
import com.witcurve.service.UserMobileEndPointService;
import com.witcurve.service.dto.UserMobileEndPointDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserMobileEndPointResource {

    private final Logger log = LoggerFactory.getLogger(UserMobileEndPointResource.class);

    @Autowired
    UserMobileEndPointService userMobileEndPointService;

    /**
     * creates userMobileEndPoint
     * @param userMobileEndPointDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/user-mobile-end-points")
    @Timed
    public ResponseEntity<UserMobileEndPointDTO> create(@RequestBody UserMobileEndPointDTO userMobileEndPointDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to create userMobileEndPoint", userMobileEndPointDTO);

        try {
            UserMobileEndPointDTO result = userMobileEndPointService.addEndPoint(userMobileEndPointDTO);
            return ResponseEntity.created(new URI("/api/user-mobile-end-point"))
                .headers(HeaderUtil.createEntityCreationAlert("userMobileEndPoints", null))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

    /**
     * get userMobileEndPoint by id
     * @param userMobileEndPointId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/user-mobile-end-points/{userMobileEndPointId}")
    @Timed
    public ResponseEntity<UserMobileEndPointDTO> getUserMobileEndPointById(@PathVariable("userMobileEndPointId") Long userMobileEndPointId) throws WitcurveException {
        log.debug("Request to get UserMobileEndPoint with id {}", userMobileEndPointId);
        UserMobileEndPointDTO result = userMobileEndPointService.getByUserMobileEndPointId(userMobileEndPointId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get all userMobileEndPoints
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/user-mobile-end-points")
    @Timed
    public ResponseEntity<List<UserMobileEndPointDTO>> getAllUserMobileEndPoints() throws WitcurveException {
        log.debug("Request to get all UserMobileEndPoints");
        List<UserMobileEndPointDTO> result = userMobileEndPointService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete a user mobile end point
     * @param userId
     * @param deviceToken
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/user-mobile-end-points/user/{userId}")
    @Timed
    public ResponseEntity<Void> deleteUserMobileEndPoint(@PathVariable Long userId, @RequestParam String deviceToken) throws WitcurveException {
        log.debug("REST request to delete userMobileEndPoint for user with id: {} and of token : {}", userId, deviceToken);
        userMobileEndPointService.deleteEndPoint(userId, deviceToken);
        return ResponseEntity.ok(null);
    }



}
