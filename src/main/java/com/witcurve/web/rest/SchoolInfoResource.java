package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.security.PermissionsConstants;
import com.witcurve.service.SchoolInfoService;
import com.witcurve.service.dto.SchoolInfoDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SchoolInfoResource {

    private final Logger log = LoggerFactory.getLogger(SchoolInfoResource.class);

    @Autowired
    SchoolInfoService schoolInfoService;

    /**
     * creates a schoolInfo
     *
     * @param schoolInfoDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @PostMapping("/school-info")
    @Timed
    public ResponseEntity<SchoolInfoDTO> createSchoolInfo(@RequestBody @Valid SchoolInfoDTO schoolInfoDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save schoolInfo");
        if (schoolInfoDTO.getId() != null) {
            throw new WitcurveException("New SchoolInfo can't already have an id");
        }
        try {
            SchoolInfoDTO result = schoolInfoService.saveOrUpdate(schoolInfoDTO);
            return ResponseEntity.created(new URI("/api/school-info/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("schoolInfo", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("board_medium_school_UK")) {
                throw new WitcurveException("Unique constraint (board, medium, school_id) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * update the given schoolInfo
     *
     * @param schoolInfoDTO
     * @return
     * @throws WitcurveException
     */

    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS +
        "') or hasAuthority('" + PermissionsConstants.INSTITUTE_FULL_ACCESS + "')")
    @PutMapping("/school-info")
    @Timed
    public ResponseEntity<SchoolInfoDTO> updateSchoolInfo(@RequestBody @Valid SchoolInfoDTO schoolInfoDTO) throws WitcurveException {
        log.debug("Request to update schoolInfo");
        if (schoolInfoDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        } else {
            schoolInfoService.getSchoolInfoById(schoolInfoDTO.getId());
        }
        try {
            SchoolInfoDTO result = schoolInfoService.saveOrUpdate(schoolInfoDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("schoolInfo", schoolInfoDTO.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("board_medium_school_UK")) {
                throw new WitcurveException("Unique constraint (board, medium, school_id) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get schoolInfo by id
     *
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     */
//    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS +
//        "') or hasAuthority('" + PermissionsConstants.INSTITUTE_FULL_ACCESS + "')")
    @GetMapping("/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<SchoolInfoDTO> getSchoolInfoById(@PathVariable("schoolInfoId") Long schoolInfoId) throws WitcurveException {
        log.debug("Request to get SchoolInfo with id {}", schoolInfoId);
        SchoolInfoDTO result = schoolInfoService.getSchoolInfoById(schoolInfoId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get schoolInfo list by school id
     *
     * @param schoolId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/school-info/schools/{schoolId}")
    @Timed
    public ResponseEntity<List<SchoolInfoDTO>> getSchoolInfosBySchoolId(@PathVariable("schoolId") Long schoolId) throws WitcurveException {
        log.debug("Request to get SchoolInfos with school id {}", schoolId);
        List<SchoolInfoDTO> result = schoolInfoService.getSchoolInfosBySchoolId(schoolId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the schoolInfo
     *
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<Void> deleteSchoolInfo(@PathVariable Long schoolInfoId) throws WitcurveException {
        log.debug("REST request to delete schoolInfo: {}", schoolInfoId);
        try {
            schoolInfoService.deleteSchoolInfo(schoolInfoId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A schoolInfo is deleted with id " + schoolInfoId,
                schoolInfoId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }

    }

    /**
     * convert to primaryBoard
     *
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS +
        "') or hasAuthority('" + PermissionsConstants.INSTITUTE_FULL_ACCESS + "')")
    @PatchMapping("/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<SchoolInfoDTO> changeToPrimaryBoard(@PathVariable("schoolInfoId") Long schoolInfoId) {
        log.debug("REST request to update schoolInfo: {}", schoolInfoId);
        SchoolInfoDTO result = schoolInfoService.changeToPrimaryBoard(schoolInfoId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
