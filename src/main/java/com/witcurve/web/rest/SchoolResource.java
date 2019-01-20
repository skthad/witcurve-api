package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SchoolService;
import com.witcurve.service.dto.SchoolDTO;
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
public class SchoolResource {

    private final Logger log = LoggerFactory.getLogger(SchoolResource.class);

    @Autowired
    SchoolService schoolService;

    /**
     * creates a school
     * @param schoolDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/schools")
    @Timed
    public ResponseEntity<SchoolDTO> createSchool(@RequestBody @Valid SchoolDTO schoolDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save school");
        if (schoolDTO.getId() != null) {
            throw new WitcurveException("New School can't already have an id");
        }
        SchoolDTO result = schoolService.saveOrUpdate(schoolDTO);
        return ResponseEntity.created(new URI("/api/schools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("school", result.getId().toString()))
            .body(result);
    }

    /**
     * update the given school
     * @param schoolDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/schools")
    @Timed
    public ResponseEntity<SchoolDTO> updateSchool(@RequestBody @Valid SchoolDTO schoolDTO) throws WitcurveException {
        log.debug("Request to update school");
        if (schoolDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        } else {
            schoolService.getSchoolById(schoolDTO.getId());
        }
        SchoolDTO result = schoolService.saveOrUpdate(schoolDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("school", schoolDTO.getId().toString()))
            .body(result);
    }

    /**
     * get school by id
     * @param schoolId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/schools/{schoolId}")
    @Timed
    public ResponseEntity<SchoolDTO> getSchoolById(@PathVariable("schoolId") Long schoolId) throws WitcurveException {
        log.debug("Request to get School with id {}", schoolId);
        SchoolDTO result = schoolService.getSchoolById(schoolId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get school by instituteId
     * @param instituteId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/schools/institutes/{instituteId}")
    @Timed
    public ResponseEntity<List<SchoolDTO>> getSchoolsInstituteId(@PathVariable("instituteId") Long instituteId) throws WitcurveException {
        log.debug("Request to get Schools with instituteId {}", instituteId);
        List<SchoolDTO> result = schoolService.getSchoolByInstituteId(instituteId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the school
     * @param schoolId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/schools/{schoolId}")
    @Timed
    public ResponseEntity<Void> deleteSchool(@PathVariable Long schoolId) throws WitcurveException {
        log.debug("REST request to delete school: {}", schoolId);
        try {
            schoolService.deleteSchool(schoolId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A school is deleted with identifier " + schoolId,
                schoolId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

}
