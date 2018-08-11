package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.AssignmentService;
import com.witcurve.service.dto.AssignmentDTO;
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
import java.util.List;

@RestController
@RequestMapping("/api")
public class AssignmentResource {

    private final Logger log = LoggerFactory.getLogger(AssignmentResource.class);

    @Autowired
    AssignmentService assignmentService;

    /**
     * creates a assignment
     * @param assignmentDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/assignment")
    @Timed
    public ResponseEntity<AssignmentDTO> createAssignment(@RequestBody @Valid AssignmentDTO assignmentDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Assignment");
        if (assignmentDTO.getId() != null) {
            throw new WitcurveException("New Assignment can't already have an id");
        }
        AssignmentDTO result = assignmentService.saveOrUpdate(assignmentDTO);
        return ResponseEntity.created(new URI("/api/assignment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("assignment", result.getId().toString()))
            .body(result);
    }

    /**
     * get assignment by id
     * @param assignmentId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/assignment/{assignmentId}")
    @Timed
    public ResponseEntity<AssignmentDTO> getAssignmentById(@PathVariable("assignmentId") Long assignmentId) throws WitcurveException {
        log.debug("Request to get Assignment with id {}", assignmentId);
        AssignmentDTO result = assignmentService.getAssignmentById(assignmentId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given assignment
     * @param assignmentDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/assignment")
    @Timed
    public ResponseEntity<AssignmentDTO> updateAssignment(@RequestBody @Valid AssignmentDTO assignmentDTO) throws WitcurveException {
        log.debug("Request to update assignment");
        if (assignmentDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        AssignmentDTO result = assignmentService.saveOrUpdate(assignmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("assignment", assignmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the assignment
     * @param assignmentId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/assignment/{assignmentId}")
    @Timed
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long assignmentId) throws WitcurveException {
        log.debug("REST request to delete Assignment: {}", assignmentId);
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("An assignment is deleted with identifier " + assignmentId,
            assignmentId.toString())).build();
    }

    /**
     *
     * @param postedDate
     * @param timeTableUnitId
     * @return
     */

    @GetMapping("/assignment/posted-on/{timeTableUnitId}")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByPostedDateAndTimeTableUnitId(
        @RequestParam("postedDate")String postedDate, @PathVariable("timeTableUnitId") Long timeTableUnitId) {
        log.debug("REST Request to get assignments posted on given date : {} and for time table unit id : {}", postedDate, timeTableUnitId);
        List<AssignmentDTO> assigments = assignmentService.findAssignmentsByPostedDateAndTimeTableUnitId(postedDate, timeTableUnitId);
        return new ResponseEntity<>(assigments, HttpStatus.OK);
    }

    /**
     *
     * @param submissionDate
     * @param timeTableUnitId
     * @return
     */

    @GetMapping("/assignment/submission-on/{timeTableUnitId}")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsBySubmissionDateAndTimeTableUnitId(
        @RequestParam("submissionDate")String submissionDate, @PathVariable("timeTableUnitId") Long timeTableUnitId) {
        log.debug("REST Request to get assignments with submission on given date : {} and for time table unit id : {}", submissionDate, timeTableUnitId);
        List<AssignmentDTO> assignments = assignmentService.findAssignmentsBySubmissionDateAndTimeTableUnitId(submissionDate, timeTableUnitId);
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }


}
