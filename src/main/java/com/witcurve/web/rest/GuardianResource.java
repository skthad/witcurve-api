package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.GuardianService;
import com.witcurve.service.dto.GuardianDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class GuardianResource {

    private final Logger log = LoggerFactory.getLogger(GuardianResource.class);

    @Autowired
    GuardianService guardianService;

    /**
     * creates a guardian
     * @param guardianDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/guardian")
    @Timed
    public ResponseEntity<GuardianDTO> createGuardian(@RequestBody @Valid GuardianDTO guardianDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Guardian");
        if (guardianDTO.getId() != null) {
            throw new WitcurveException("New guardian can't already have an id");
        }
        GuardianDTO result = guardianService.saveOrUpdate(guardianDTO);
        return ResponseEntity.created(new URI("/api/guardian/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("guardian", result.getId().toString()))
            .body(result);
    }

    /**
     * get guardian by id
     * @param guardianId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/guardian/{guardianId}")
    @Timed
    public ResponseEntity<GuardianDTO> getGuardianById(@PathVariable("guardianId") Long guardianId) throws WitcurveException {
        log.debug("Request to get guardian with id {}", guardianId);
        GuardianDTO result = guardianService.getGuardianById(guardianId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given Guardian
     * @param guardianDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/guardian")
    @Timed
    public ResponseEntity<GuardianDTO> updateGuardian(@RequestBody @Valid GuardianDTO guardianDTO) throws WitcurveException {
        log.debug("Request to update guardian");
        if (guardianDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        GuardianDTO result = guardianService.saveOrUpdate(guardianDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("guardian", guardianDTO.getId().toString()))
            .body(result);
    }
}
