package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.security.PermissionsConstants;
import com.witcurve.service.InstituteService;
import com.witcurve.service.dto.InstituteDTO;
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
public class InstituteResource {

    private final Logger log = LoggerFactory.getLogger(InstituteResource.class);

    @Autowired
    InstituteService instituteService;

    /**
     * creates a institute
     * @param instituteDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/institutes")
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @Timed
    public ResponseEntity<InstituteDTO> createInstitute(@RequestBody @Valid InstituteDTO instituteDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save institute");
        if (instituteDTO.getId() != null) {
            throw new WitcurveException("New Institute can't already have an id");
        }
        try {
            InstituteDTO result = instituteService.saveOrUpdate(instituteDTO);
            return ResponseEntity.created(new URI("/api/institutes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("institute", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("institute_name_UK")) {
                throw new WitcurveException("Unique constraint (name) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * update the given institute
     * @param instituteDTO
     * @return
     * @throws WitcurveException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS +
    "') or hasAuthority('" + PermissionsConstants.INSTITUTE_FULL_ACCESS + "')")
    @PutMapping("/institutes")
    @Timed
    public ResponseEntity<InstituteDTO> updateInstitute(@RequestBody @Valid InstituteDTO instituteDTO) throws WitcurveException {
        log.debug("Request to update institute");
        if (instituteDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        } else {
            instituteService.getInstituteById(instituteDTO.getId());
        }
        try {
            InstituteDTO result = instituteService.saveOrUpdate(instituteDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("institute", instituteDTO.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("institute_name_UK")) {
                throw new WitcurveException("Unique constraint (name) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get institute by id
     * @param instituteId
     * @return
     * @throws WitcurveException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @GetMapping("/institutes/{instituteId}")
    @Timed
    public ResponseEntity<InstituteDTO> getInstituteById(@PathVariable("instituteId") Long instituteId) throws WitcurveException {
        log.debug("Request to get Institute with id {}", instituteId);
        InstituteDTO result = instituteService.getInstituteById(instituteId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get all institutes
     * @return
     * @throws WitcurveException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @GetMapping("/institutes")
    @Timed
    public ResponseEntity<List<InstituteDTO>> getAllInsitutes() throws WitcurveException {
        log.debug("Request to get all Institutes with id");
        List<InstituteDTO> result = instituteService.getAllInstitutes();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the institute
     * @param instituteId
     * @return
     * @throws WitcurveException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @DeleteMapping("/institutes/{instituteId}")
    @Timed
    public ResponseEntity<Void> deleteInstitute(@PathVariable Long instituteId) throws WitcurveException {
        log.debug("REST request to delete institute: {}", instituteId);
        try {
            instituteService.deleteInstitute(instituteId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A institute is deleted with id " + instituteId,
                instituteId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

}
