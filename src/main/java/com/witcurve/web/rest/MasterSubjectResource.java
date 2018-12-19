package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.MasterSubject;
import com.witcurve.service.MasterSubjectService;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<MasterSubject> createMasterSubject(@RequestBody @Valid MasterSubject masterSubject) throws WitcurveException, URISyntaxException {
        log.debug("Request Save MasterSubject");
        MasterSubject result = masterSubjectService.saveOrUpdate(masterSubject);
        return ResponseEntity.created(new URI("/api/master-subject/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert("masterSubject", result.getName().toString()))
            .body(result);
    }

    /**
     * delete the masterSubject
     * @param name
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/master-subject/{name}")
    @Timed
    public ResponseEntity<Void> deleteMasterSubject(@PathVariable String name) throws WitcurveException {
        log.debug("REST request to delete MasterSubject: {}", name);
        masterSubjectService.deleteMasterSubject(name);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A masterSubject is deleted with name " + name,
            name)).build();
    }

}
