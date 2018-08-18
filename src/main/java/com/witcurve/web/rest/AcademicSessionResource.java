package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.AcademicSessionService;
import com.witcurve.service.dto.AcademicSessionDTO;
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

@RestController
@RequestMapping("/api")
public class AcademicSessionResource {

    private final Logger log = LoggerFactory.getLogger(AcademicSessionResource.class);

    @Autowired
    AcademicSessionService academicSessionService;

    /**
     * creates a academic session
     * @param academicSessionDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/academic-session")
    @Timed
    public ResponseEntity<AcademicSessionDTO> createAcademicSession(@RequestBody @Valid AcademicSessionDTO academicSessionDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Academic Session");
        if (academicSessionDTO.getId() != null) {
            throw new WitcurveException("New Academic Session can't already have an id");
        }
        AcademicSessionDTO result = academicSessionService.saveOrUpdate(academicSessionDTO);
        return ResponseEntity.created(new URI("/api/academic-session/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("academicSession", result.getId().toString()))
            .body(result);
    }

    /**
     * get academic session by id
     * @param academicSessionId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/academic-session/{academicSessionId}")
    @Timed
    public ResponseEntity<AcademicSessionDTO> getAcademicSessionById(@PathVariable("academicSessionId") Long academicSessionId) throws WitcurveException {
        log.debug("Request to get Academic Session with id {}", academicSessionId);
        AcademicSessionDTO result = academicSessionService.getAcademicSessionById(academicSessionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given academic session
     * @param academicSessionDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/academic-session")
    @Timed
    public ResponseEntity<AcademicSessionDTO> updateAcademicSession(@RequestBody @Valid AcademicSessionDTO academicSessionDTO) throws WitcurveException {
        log.debug("Request to update academicSession");
        if (academicSessionDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        AcademicSessionDTO result = academicSessionService.saveOrUpdate(academicSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("academicSession", academicSessionDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the academicSession
     * @param academicSessionId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/academic-session/{academicSessionId}")
    @Timed
    public ResponseEntity<Void> deleteAcademicSession(@PathVariable Long academicSessionId) throws WitcurveException {
        log.debug("REST request to delete AcademicSession: {}", academicSessionId);
        academicSessionService.deleteAcademicSession(academicSessionId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A academicSession is deleted with identifier " + academicSessionId,
            academicSessionId.toString())).build();
    }
}
