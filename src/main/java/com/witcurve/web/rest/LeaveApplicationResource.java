package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.service.EventService;
import com.witcurve.service.LeaveApplicationService;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LeaveApplicationResource {

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationResource.class);

    @Autowired
    LeaveApplicationService leaveApplicationService;

    @Autowired
    EventService eventService;

    /**
     * creates a leave-applications
     *
     * @param leaveApplicationDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/leave-application")
    @Timed
    public ResponseEntity<LeaveApplicationDTO> createLeaveApplication(@RequestBody @Valid LeaveApplicationDTO leaveApplicationDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Leave Application", leaveApplicationDTOs);
        if (leaveApplicationDTOs.getId() == null) {
            LeaveApplicationDTO result = leaveApplicationService.saveOrUpdate(leaveApplicationDTOs, false);
            return ResponseEntity.ok(result);
        } else {
            throw new WitcurveException("New leave application can't already have an id");
        }
    }

    /**
     * get leave-application by id
     *
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
     *
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
        LeaveApplicationDTO result = leaveApplicationService.saveOrUpdate(leaveApplicationDTO, true);
        return ResponseEntity.ok().body(result);
    }

    /**
     * delete the leave-application
     *
     * @param leaveApplicationId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/leave-application/{leaveApplicationId}")
    @Timed
    public ResponseEntity<Void> deleteLeaveApplication(@PathVariable Long leaveApplicationId) throws WitcurveException {
        log.debug("REST request to delete leave application: {}", leaveApplicationId);

        leaveApplicationService.deleteLeaveApplication(leaveApplicationId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A leave application is deleted with identifier " + leaveApplicationId,
            leaveApplicationId.toString())).build();
    }

    /**
     * get approval by Staff
     *
     * @param applicationId,staffId
     * @return
     * @throws WitcurveException
     */

    @PatchMapping("/leave-application/{leaveApplicationId}")
    @Timed
    public ResponseEntity<LeaveApplicationDTO> getLeaveApplicationApproval(
        @PathVariable("leaveApplicationId") Long applicationId,
        @RequestParam Long staffId,
        @RequestParam ApprovalStatus status,
        @RequestParam(required = false) String note) throws WitcurveException {
        log.debug("The LeaveApplication approved by staff id {} for application with id : {}", staffId, applicationId);
        try {
            LeaveApplicationDTO result = leaveApplicationService.changeLeaveStatus(applicationId, staffId, status, note);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get leave-application of staff by id
     *
     * @param staffId,sessionId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/staff/{staffId}")
    @Timed
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApplicationByStaffIdAndSessionId
    (@PathVariable Long staffId, @RequestParam Long sessionId,
     @RequestParam(required = false) ApprovalStatus status) throws WitcurveException {
        log.debug("Request to get LeaveApplication with session id : {} and staff id : {}", sessionId, staffId);
        List<LeaveApplicationDTO> result = leaveApplicationService.getLeaveApplicationsForStaff(staffId, sessionId, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get leave-application of student by id
     *
     * @param studentId,sessionId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/student/{studentId}")
    @Timed
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApplicationByStudentIdAndSessionId
    (@PathVariable Long studentId, @RequestParam Long sessionId,
     @RequestParam(required = false) ApprovalStatus status) throws WitcurveException {
        log.debug("Request to get LeaveApplication with session id : {} and student id : {}", sessionId, studentId);
        List<LeaveApplicationDTO> result = leaveApplicationService.getLeaveApplicationsForStudent(studentId, sessionId, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get leave-application for standard by id
     *
     * @param standardId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/standard/{standardId}")
    @Timed
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApplicationByStandardIdAndSessionId
    (@PathVariable Long standardId, @RequestParam(required = false) ApprovalStatus status) throws WitcurveException {
        log.debug("Request to get LeaveApplication with standard id : {}", standardId);
        List<LeaveApplicationDTO> result = leaveApplicationService.getLeaveApplicationsForStandard(standardId, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get leave-application count
     *
     * @param fromDate
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application/leave-count")
    @Timed
    public ResponseEntity<Long> getLeaveCount(@RequestParam(name = "fromDate") LocalDate fromDate, @RequestParam(name = "toDate") LocalDate toDate, @RequestParam Long schoolInfoId, @RequestParam Boolean isSaturdayWorking) throws WitcurveException {
        log.debug("Request to get number of leaves from date : {}", fromDate, " to date :", toDate);
        Long workingDays = leaveApplicationService.workingDays(fromDate, toDate, schoolInfoId, isSaturdayWorking);
        return new ResponseEntity<>(workingDays, HttpStatus.OK);
    }

    /**
     * Validate existence of leave application
     *
     * @param fromDate
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/leave-application")
    @Timed
    public ResponseEntity<Map<Long,List<LeaveApplicationDTO>>> getExistingLeaveApplicationDetails
    (@RequestParam(name = "fromDate") LocalDate fromDate, @RequestParam(name = "toDate") LocalDate toDate, @RequestParam(required = false) Long studentId,
     @RequestParam(required = false) Long staffId, @RequestParam(required = false) Long schoolInfoId) throws WitcurveException {
        List<LeaveApplicationDTO> result = leaveApplicationService.getAppliedLeaveDetails(studentId, staffId, schoolInfoId, fromDate, toDate);
        Map<Long, List<LeaveApplicationDTO>> resultMap = new HashMap<>();
        if (studentId != null) {
            resultMap.put(studentId,result);
        } else if (staffId != null) {
            resultMap.put(staffId,result);
        } else if (schoolInfoId != null) {
            for (LeaveApplicationDTO leaveApplicationDTO : result) {
                    List<LeaveApplicationDTO> leaveApplicationDTOs = resultMap.get(leaveApplicationDTO.getAppliedStaffId());
                    if (leaveApplicationDTOs == null) {
                        leaveApplicationDTOs = new ArrayList<>();
                    }
                    leaveApplicationDTOs.add(leaveApplicationDTO);
                    resultMap.put(leaveApplicationDTO.getAppliedStaffId(), leaveApplicationDTOs);
                }
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}
