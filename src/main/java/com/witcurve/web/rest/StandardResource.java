package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.StandardService;
import com.witcurve.service.dto.StandardDTO;
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
import java.util.List;

@RestController
@RequestMapping("/api")
public class StandardResource {

    private final Logger log = LoggerFactory.getLogger(StandardResource.class);

    @Autowired
    StandardService standardService;

    /**
     * creates a new standard
     * @param standardDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/standard")
    @Timed
    public ResponseEntity<StandardDTO> createStandard(@RequestBody @Valid StandardDTO standardDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request create standard");
        if (standardDTO.getId() != null) {
            throw new WitcurveException("New Standard can't already have an id");
        }
        StandardDTO result = standardService.saveOrUpdateStandard(standardDTO);
        return ResponseEntity.created(new URI("/api/standard/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("standard", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/standard")
    @Timed
    public ResponseEntity<StandardDTO> updateStandard(@RequestBody @Valid StandardDTO standardDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request create standard");
        if (standardDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        StandardDTO result = standardService.saveOrUpdateStandard(standardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("standard", standardDTO.getId().toString()))
            .body(result);
    }

    /**
     * get standard by id
     * @param standardId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/standard/{standardId}")
    @Timed
    public ResponseEntity<StandardDTO> getStandardById(@PathVariable("standardId") Long standardId) throws WitcurveException {
        log.debug("Request to get standard by id");
        StandardDTO result = standardService.getStandardById(standardId);
        return ResponseEntity.ok(result);
    }

    /**
     * get standards by school info id
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/standard/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<StandardDTO>> getStandardsBySchoolInfoId(@PathVariable("schoolInfoId") Long schoolInfoId) throws WitcurveException {
        log.debug("Request to get standards by school info id: {}", schoolInfoId);
        List<StandardDTO> result = standardService.getStandardsBySchoolInfoId(schoolInfoId);
        return ResponseEntity.ok(result);
    }

    /**
     * get standards by teacher ID
     * @param teacherId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/standard/teacher/{teacherId}")
    @Timed
    public ResponseEntity<List<StandardDTO>> getCoursesByTeacherIdAndTermId(
        @PathVariable(value = "teacherId") Long teacherId,
        @RequestParam(value = "termId", required = false) Long termId) throws WitcurveException {
        log.debug("Request to get standards with teacher id {}", teacherId);
        List<StandardDTO> result = standardService.getStandardsByTeacherIdAndTermId(teacherId, termId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the standard
     * @param standardId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/standard/{standardId}")
    @Timed
    public ResponseEntity<Void> deleteStandard(@PathVariable Long standardId) throws WitcurveException {
        log.debug("REST request to delete Standard: {}", standardId);
        standardService.deleteStandard(standardId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A standard is deleted with identifier " + standardId,
            standardId.toString())).build();
    }


}
