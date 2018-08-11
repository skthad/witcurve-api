package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.MasterSubject;
import com.witcurve.service.MasterSubjectService;
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
public class MasterSubjectResource {

    private final Logger log = LoggerFactory.getLogger(MasterSubjectResource.class);

    @Autowired
    MasterSubjectService masterSubjectService;

    /**
     * creates a MasterSubject
     * @param masterSubject
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/master-subject")
    @Timed
    public ResponseEntity<MasterSubject> createExamDetails(@RequestBody @Valid MasterSubject masterSubject) throws WitcurveException, URISyntaxException {
        log.debug("Request Save MasterSubject");
        if (masterSubject.getId() != null) {
            throw new WitcurveException("New MasterSubject can't already have an id");
        }
        MasterSubject result = masterSubjectService.saveOrUpdate(masterSubject);
        return ResponseEntity.created(new URI("/api/master-subject/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("masterSubject", result.getId().toString()))
            .body(result);
    }

    /**
     * get MasterSubject by id
     * @param masterSubjectId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/master-subject/{masterSubjectId}")
    @Timed
    public ResponseEntity<MasterSubject> getExamDetailsById(@PathVariable("masterSubjectId") Long masterSubjectId) throws WitcurveException {
        log.debug("Request to get ExamDetails with id {}", masterSubjectId);
        MasterSubject result = masterSubjectService.getMasterSubjectId(masterSubjectId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given masterSubject
     * @param masterSubject
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/master-subject")
    @Timed
    public ResponseEntity<MasterSubject> updateExamDetails(@RequestBody @Valid MasterSubject masterSubject) throws WitcurveException {
        log.debug("Request to update MasterSubject");
        if (masterSubject.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        MasterSubject result = masterSubjectService.saveOrUpdate(masterSubject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("masterSubject", masterSubject.getId().toString()))
            .body(result);
    }

}
