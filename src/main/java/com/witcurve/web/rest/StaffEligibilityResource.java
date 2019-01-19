package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.StaffEligibility;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.StaffEligibilityService;
import com.witcurve.service.dto.StaffEligibilityDTO;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StaffEligibilityResource {


    private final Logger log = LoggerFactory.getLogger(StaffEligibility.class);

    @Autowired
    StaffEligibilityService staffEligibilityService;

    /**
     * creates a staffEligibility
     * @param staffEligibilityDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/staff-eligibility")
    @Timed
    public ResponseEntity<StaffEligibilityDTO> createStaffEligibility(@RequestBody @Valid StaffEligibilityDTO staffEligibilityDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save staffEligibility");
        if (staffEligibilityDTO.getId() != null) {
            throw new WitcurveException("New staffEligibility can't already have an id");
        }

        try {
            StaffEligibilityDTO result = staffEligibilityService.saveOrUpdate(staffEligibilityDTO);
            return ResponseEntity.created(new URI("/api/staff-eligibility/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("staffEligibility", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("staff_eligibility_subject_staff_grade_UK")) {
                throw new WitcurveException("Unique constraint (master_subject, staff_id, grade) violated");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

    /**
     * update the given staffEligibility
     * @param staffEligibilityDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/staff-eligibility")
    @Timed
    public ResponseEntity<StaffEligibilityDTO> updateStaffEligibility(@RequestBody @Valid StaffEligibilityDTO staffEligibilityDTO) throws WitcurveException {
        log.debug("Request to update staffEligibility");
        if (staffEligibilityDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }

        try {
            StaffEligibilityDTO result = staffEligibilityService.saveOrUpdate(staffEligibilityDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("staffEligibility", staffEligibilityDTO.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("staff_eligibility_subject_staff_grade_UK")) {
                throw new WitcurveException("Unique constraint (master_subject, staff_id, grade) violated");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

    /**
     * get staffEligibility by teacherId
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/staff-eligibility/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<StaffEligibilityDTO>> getStaffEligibilitysBySchoolInfo(@PathVariable(value = "schoolInfoId") Long schoolInfoId,
                                                                                      @RequestParam(value = "subject", required = false) String subject,
                                                                                      @RequestParam(value = "grade", required = false) Grade grade) throws WitcurveException {

        log.debug("Request to get StaffEligibility with schoolInfoId {}", schoolInfoId);

        List<StaffEligibilityDTO> result = staffEligibilityService.getStaffEligibilitysBySchoolInfo(schoolInfoId, subject, grade);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/staff-eligibility/staff/{staffId}")
    @Timed
    public ResponseEntity<List<StaffEligibilityDTO>> getStaffEligibilitysByStaff(@PathVariable("staffId") Long staffId,
                                                                                 @RequestParam(value = "subject", required = false) String subject,
                                                                                 @RequestParam(value = "grade", required = false) Grade grade) throws WitcurveException {
        log.debug("Request to get Course with staffId {}", staffId);
        List<StaffEligibilityDTO> result = staffEligibilityService.getStaffEligibilitysByStaff(staffId, subject, grade);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the staffEligibility
     * @param staffEligibilityId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/staff-eligibility/{staffEligibilityId}")
    @Timed
    public ResponseEntity<Void> deleteStaffEligibility(@PathVariable Long staffEligibilityId) throws WitcurveException {
        log.debug("REST request to delete StaffEligibility: {}", staffEligibilityId);
        staffEligibilityService.deleteStaffEligibility(staffEligibilityId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A staffEligibility is deleted with identifier " + staffEligibilityId,
            staffEligibilityId.toString())).build();
    }
}
