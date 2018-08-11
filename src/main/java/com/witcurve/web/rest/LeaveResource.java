package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.LeaveService;
import com.witcurve.service.dto.LeaveDTO;
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
public class LeaveResource {

    private final Logger log = LoggerFactory.getLogger(LeaveResource.class);

    @Autowired
    LeaveService leaveService;

    /**
     * creates a leave
     * @param leaveDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/leave")
    @Timed
    public ResponseEntity<LeaveDTO> createLeave(@RequestBody @Valid LeaveDTO leaveDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Leave");
        if (leaveDTO.getId() != null) {
            throw new WitcurveException("New Leave can't already have an id");
        }
        LeaveDTO result = leaveService.saveOrUpdate(leaveDTO);
        return ResponseEntity.created(new URI("/api/leave/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("leave", result.getId().toString()))
            .body(result);
    }

    /**
     * get leave by id
     * @param leaveId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave/{leaveId}")
    @Timed
    public ResponseEntity<LeaveDTO> getLeaveById(@PathVariable("leaveId") Long leaveId) throws WitcurveException {
        log.debug("Request to get Leave with id {}", leaveId);
        LeaveDTO result = leaveService.getLeaveById(leaveId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given leave
     * @param leaveDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/leave")
    @Timed
    public ResponseEntity<LeaveDTO> updateLeave(@RequestBody @Valid LeaveDTO leaveDTO) throws WitcurveException {
        log.debug("Request to update leave");
        if (leaveDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        LeaveDTO result = leaveService.saveOrUpdate(leaveDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("leave", leaveDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the leave
     * @param leaveId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/leave/{leaveId}")
    @Timed
    public ResponseEntity<Void> deleteLeave(@PathVariable Long leaveId) throws WitcurveException {
        log.debug("REST request to delete Leave: {}", leaveId);
        leaveService.deleteLeave(leaveId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A leave is deleted with identifier " + leaveId,
            leaveId.toString())).build();
    }
}
