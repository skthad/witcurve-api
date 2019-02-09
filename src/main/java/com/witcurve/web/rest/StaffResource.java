package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StaffService;
import com.witcurve.service.dto.StaffDTO;
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
public class StaffResource {

    private final Logger log = LoggerFactory.getLogger(StaffResource.class);

    @Autowired
    StaffService staffService;

    /**
     * creates a Staff
     * @param staffDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/staff")
    @Timed
    public ResponseEntity<StaffDTO> createStaff(@RequestBody @Valid StaffDTO staffDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Staff");
        if (staffDTO.getId() != null) {
            throw new WitcurveException("New Staff can't already have an id");
        }
        StaffDTO result = staffService.create(staffDTO);
        return ResponseEntity.created(new URI("/api/staff/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("staff", result.getId().toString()))
            .body(result);
    }

    /**
     * get Staff by id
     * @param staffId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/staff/{staffId}")
    @Timed
    public ResponseEntity<StaffDTO> getStaff(@PathVariable("staffId") Long staffId) throws WitcurveException {
        log.debug("Request to get Staff with id {}", staffId);
        StaffDTO result = staffService.getStaffById(staffId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get Staff by school id
     * @param schoolId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/staff/schools/{schoolId}")
    @Timed
    public ResponseEntity<List<StaffDTO>> getStaffBySchoolId(@PathVariable("schoolId") Long schoolId) throws WitcurveException {
        log.debug("Request to get Staff with school id {}", schoolId);
        List<StaffDTO> result = staffService.getStaffBySchoolId(schoolId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get Staff by schoolInfo id
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/staff/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<StaffDTO>> getStaffBySchoolInfoId(@PathVariable("schoolInfoId") Long schoolInfoId) throws WitcurveException {
        log.debug("Request to get Staff with schoolInfo id {}", schoolInfoId);
        List<StaffDTO> result = staffService.getStaffBySchoolInfoId(schoolInfoId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given staff
     * @param staffDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/staff")
    @Timed
    public ResponseEntity<StaffDTO> updateStaff(@RequestBody @Valid StaffDTO staffDTO) throws WitcurveException {
        log.debug("Request to update Staff");
        if (staffDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        } else {
            staffService.getStaffById(staffDTO.getId());
        }
        StaffDTO result = staffService.update(staffDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("staff", staffDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the staff
     * @param staffId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/staff/{staffId}")
    @Timed
    public ResponseEntity<Void> deleteStaff(@PathVariable Long staffId) throws WitcurveException {
        log.debug("REST request to delete Staff: {}", staffId);
        try {
            staffService.deleteStaffById(staffId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A staff is deleted with identifier " + staffId,
                staffId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }
}
