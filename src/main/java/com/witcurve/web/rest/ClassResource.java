package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.ClassService;
import com.witcurve.service.dto.ClassDTO;
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
public class ClassResource {

    private final Logger log = LoggerFactory.getLogger(ClassResource.class);

    @Autowired
    ClassService classService;

    /**
     * creates a new class
     * @param classDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/class")
    @Timed
    public ResponseEntity<ClassDTO> createClass(@RequestBody @Valid ClassDTO classDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request create class");
        if (classDTO.getId() != null) {
            throw new WitcurveException("New Class can't already have an id");
        }
        ClassDTO result = classService.saveOrUpdateClass(classDTO);
        return ResponseEntity.created(new URI("/api/class/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("class", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/class")
    @Timed
    public ResponseEntity<ClassDTO> updateClass(@RequestBody @Valid ClassDTO classDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request create class");
        if (classDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        ClassDTO result = classService.saveOrUpdateClass(classDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("class", classDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/class/{classId}")
    @Timed
    public ResponseEntity<ClassDTO> getClassById(@PathVariable("classId") Long classId) throws WitcurveException {
        log.debug("Request to get class by id");
        ClassDTO result = classService.getClassById(classId);
        return ResponseEntity.ok(result);
    }


}
