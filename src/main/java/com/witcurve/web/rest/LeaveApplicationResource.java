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
import java.net.URISyntaxException;
import java.util.List;
@RestController
@RequestMapping("/api")
public class LeaveApplicationResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    @Autowired
    LeaveApplicationService leaveApplicationService;

    /**
     * creates a leave-application
     * @param leaveApplicationDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/leave-application")
    @Timed
    public ResponseEntity<List<LeaveApplicationDTO>> createLeaveApplication(@RequestBody @Valid List<LeaveApplicationDTO> leaveApplicationDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Leave Application",leaveApplicationDTOs);
        for(LeaveApplicationDTO leaveApplicationDTO: leaveApplicationDTOs)
        {
            if (leaveApplicationDTO.getId() != null) {
                throw new WitcurveException("New leave application can't already have an id");
                }
        }
            List<LeaveApplicationDTO> result = leaveApplicationService.saveOrUpdate(leaveApplicationDTOs);
            return ResponseEntity.ok(result);
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
     * update the given leave-application
     * @param leaveApplicationDTOs
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/leave-application")
    @Timed
    public ResponseEntity<List<LeaveApplicationDTO>> updateLeaveApplication(@RequestBody @Valid List<LeaveApplicationDTO> leaveApplicationDTOs) throws WitcurveException {
        log.debug("Request to update leave application");
        for (LeaveApplicationDTO leaveApplicationDTO : leaveApplicationDTOs) {
            if (leaveApplicationDTO.getId() == null) {
                throw new WitcurveException("Id is required for update request");
            }
        }
            List<LeaveApplicationDTO> result = leaveApplicationService.saveOrUpdate(leaveApplicationDTOs);
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

    @GetMapping("/leave-application/{leaveApplicationId}/application")
    @Timed
    public ResponseEntity<LeaveApplicationDTO> getLeaveApplicationApproval(@PathVariable("leaveApplicationId") Long applicationId,@RequestParam Long staffId) throws WitcurveException {
        log.debug("The LeaveApplication approved by staff id {}", staffId, "for application",applicationId);
        LeaveApplicationDTO result = leaveApplicationService.getLeaveApplicationApprover(applicationId,staffId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get leave-application of staff by id
     * @param staffId,sessionId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/staff")
    @Timed
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApplicationByStaffIdAndSessionId(@RequestParam Long staffId,@RequestParam Long sessionId) throws WitcurveException {
        log.debug("Request to get LeaveApplication with session id {}", sessionId," and staff id {}", staffId);
        List<LeaveApplicationDTO> result = leaveApplicationService.getLeaveApplicationsForStaff(staffId,sessionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get leave-application of student by id
     * @param studentId,sessionId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/student")
    @Timed
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApplicationByStudentIdAndSessionId(@RequestParam Long studentId,@RequestParam Long sessionId) throws WitcurveException {
        log.debug("Request to get LeaveApplication with id {}", sessionId," and student id {}", studentId);
        List<LeaveApplicationDTO> result = leaveApplicationService.getLeaveApplicationsForStudent(studentId,sessionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
