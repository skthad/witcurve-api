package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.Authority;
import com.witcurve.security.PermissionsConstants;
import com.witcurve.service.InstituteService;
import com.witcurve.service.dto.AuthorityDTO;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthorityResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);

    @Autowired
    InstituteService instituteService;

    /**
     * creates/updates custom authority for an institute
     * @param instituteId
     * @param authorityDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @PostMapping("/authorities/institutes/{instituteId}")
    @Timed
    public ResponseEntity<Authority> saveOrUpdateInstituteAuthority(@RequestBody AuthorityDTO authorityDTO,
                                                                   @PathVariable Long instituteId) throws WitcurveException, URISyntaxException {
        log.debug("Request to save custom authority for institute id: " + instituteId);
        try {
            Authority result = instituteService.saveOrUpdateCustomAuthority(instituteId, authorityDTO);
            return ResponseEntity.created(new URI("/api/institutes/" + instituteId))
                .headers(HeaderUtil.createEntityCreationAlert("institute", instituteId.toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            throw new WitcurveException("DataIntegrityViolationException occurred.");
        }
    }

    /**
     * gets authorities list
     * @param instituteId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @GetMapping("/authorities")
    @Timed
    public ResponseEntity<List<Authority>> getAuthorities(@RequestParam Long instituteId) throws WitcurveException, URISyntaxException {
        log.debug("Request to get authorites for institute");
        List<Authority> result = instituteService.getAuthorities(instituteId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * deletes custom authority for an institute
     * @param instituteId
     * @param name
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PreAuthorize("hasAuthority('" + PermissionsConstants.SUPER_ACCESS + "')")
    @DeleteMapping("/authorities/institutes/{instituteId}/")
    @Timed
    public ResponseEntity<Authority> deleteInstituteAuthority(@PathVariable Long instituteId,
                                                                @RequestParam String name) throws WitcurveException, URISyntaxException {
        log.debug("Request to delete custom authority for institute id: " + instituteId);
        try {
            instituteService.deleteCustomAuthority(instituteId, name);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("Authority is deleted",
                name)).build();
        } catch (DataIntegrityViolationException e) {
            throw new WitcurveException("DataIntegrityViolationException occurred.");
        }
    }

}
