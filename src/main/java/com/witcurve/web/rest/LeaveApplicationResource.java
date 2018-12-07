package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.LeaveApplicationService;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.service.impl.ExamServiceImpl;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URISyntaxException;
import java.util.List;
@RestController
@RequestMapping("/api")
public class LeaveApplicationResource {

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationResource.class);

    @Autowired
    LeaveApplicationService leaveApplicationService;

    /**
     * creates a leave-applications
     * @param leaveApplicationDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/leave-application")
    @Timed
    public ResponseEntity<LeaveApplicationDTO> createLeaveApplication(@RequestBody @Valid LeaveApplicationDTO leaveApplicationDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Leave Application",leaveApplicationDTOs);
            if (leaveApplicationDTOs.getId() == null) {
                LeaveApplicationDTO result = leaveApplicationService.saveOrUpdate(leaveApplicationDTOs);
                return ResponseEntity.ok(result);
                }
            else {
                throw new WitcurveException("New leave application can't already have an id");
            }
    }
    /**
     * get leave-application by id
     * @param leaveApplicationId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/{leaveApplicationId}")
    @Timed
    public ResponseEntity<LeaveApplicationDTO> getLeaveApplicationById(@PathVariable("leaveApplicationId") Long leaveApplicationId) throws WitcurveException {
        log.debug("Request to get LeaveApplication with id {}", leaveApplicationId);
        LeaveApplicationDTO result = leaveApplicationService.getLeaveApplicationById(leaveApplicationId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given leave-applications
     * @param leaveApplicationDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/leave-application")
    @Timed
    public ResponseEntity<LeaveApplicationDTO> updateLeaveApplication(@RequestBody @Valid LeaveApplicationDTO leaveApplicationDTO) throws WitcurveException {
        log.debug("Request to update leave application");
            if (leaveApplicationDTO.getId() == null) {
                throw new WitcurveException("Id is required for update request");
            }
            LeaveApplicationDTO result = leaveApplicationService.saveOrUpdate(leaveApplicationDTO);
            return ResponseEntity.ok().body(result);
    }

    /**
     * delete the leave-application
     * @param leaveApplicationId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/leave-application/{leaveApplicationId}")
    @Timed
    public ResponseEntity<Void> deleteLeaveApplication(@PathVariable Long leaveApplicationId) throws WitcurveException {
        log.debug("REST request to delete leave application: {}", leaveApplicationId);
        leaveApplicationService.deleteLeaveApplication(leaveApplicationId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A leave application is deleted with identifier " + leaveApplicationId,
            leaveApplicationId.toString())).build();
    }

    /**
     * get approval by Staff
     * @param applicationId,staffId
     * @return
     * @throws WitcurveException
     */

    @PatchMapping("/leave-application/{leaveApplicationId}/approval")
    @Timed
    public ResponseEntity<LeaveApplicationDTO> getLeaveApplicationApproval(@PathVariable("leaveApplicationId") Long applicationId,@RequestParam Long staffId) throws WitcurveException {
        log.debug("The LeaveApplication approved by staff id {} for application with id : {}",staffId, applicationId);
        LeaveApplicationDTO result = leaveApplicationService.getLeaveApplicationApprover(applicationId,staffId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get leave-application of staff by id
     * @param staffId,sessionId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/staff/{staffId}")
    @Timed
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApplicationByStaffIdAndSessionId(@PathVariable Long staffId, @RequestParam Long sessionId, @RequestParam(required = false) Boolean approved) throws WitcurveException {
        log.debug("Request to get LeaveApplication with session id : {} and staff id : {}", sessionId, staffId);
        List<LeaveApplicationDTO> result = leaveApplicationService.getLeaveApplicationsForStaff(staffId, sessionId, approved);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get leave-application of student by id
     * @param studentId,sessionId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/student/{studentId}")
    @Timed
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApplicationByStudentIdAndSessionId(@PathVariable Long studentId, @RequestParam Long sessionId, @RequestParam(required = false) Boolean approved) throws WitcurveException {
        log.debug("Request to get LeaveApplication with session id : {} and student id : {}", sessionId, studentId);
        List<LeaveApplicationDTO> result = leaveApplicationService.getLeaveApplicationsForStudent(studentId, sessionId, approved);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get leave-application for standard by id
     * @param standardId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/standard/{standardId}")
    @Timed
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApplicationByStandardIdAndSessionId(@PathVariable Long standardId,  @RequestParam(required = false) Boolean approved) throws WitcurveException {
        log.debug("Request to get LeaveApplication with standard id : {}", standardId);
        List<LeaveApplicationDTO> result = leaveApplicationService.getLeaveApplicationsForStandard(standardId, approved);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get leave-application count
     * @param applicationId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/leave-count/{leaveApplicationId}")
    @Timed
    public ResponseEntity<Long> getLeaveCount(@PathVariable(name="leaveApplicationId") Long applicationId, @RequestParam(required = false) Long sessionId, @RequestParam Boolean isSaturdayWorking) throws WitcurveException {
        log.debug("Request to get number of leaves with application id : {}", applicationId);
        Long workingDays= leaveApplicationService.getLeaveCount(applicationId,sessionId,isSaturdayWorking);
        return new ResponseEntity<>(workingDays, HttpStatus.OK);
    }
}
