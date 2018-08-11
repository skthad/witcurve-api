package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.TimeTableUnitService;
import com.witcurve.service.dto.TimeTableUnitDTO;
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
public class TimeTableUnitResource {

    private static final Logger log = LoggerFactory.getLogger(TimeTableUnitResource.class);

    @Autowired
    TimeTableUnitService timeTableUnitService;

    /**
     *
     * @param timeTableUnitDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/timetable-unit")
    @Timed
    public ResponseEntity<TimeTableUnitDTO> createTimeTableUnitDTO(@RequestBody @Valid TimeTableUnitDTO timeTableUnitDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to create time table unit ", timeTableUnitDTO);
        if (timeTableUnitDTO.getId() != null) {
            throw new WitcurveException("New time table unit can't have id");
        }
        TimeTableUnitDTO result = timeTableUnitService.saveOrUpdate(timeTableUnitDTO);
        return ResponseEntity.created(new URI("/api/timetable-unit/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("timeTableUnit", result.getId().toString()))
            .body(result);
    }

    @GetMapping("/timetable-unit/{id}")
    @Timed
    public ResponseEntity<TimeTableUnitDTO> getTimeTableUnitById(@PathVariable("id") Long timetableUnitId) throws WitcurveException {
        log.debug("REST request to get timeTableUnit by id : {}", timetableUnitId);
        TimeTableUnitDTO result = timeTableUnitService.getTimetableUnitById(timetableUnitId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/timetable-unit")
    @Timed
    public ResponseEntity<TimeTableUnitDTO> getTimeTableUnitById(@RequestBody TimeTableUnitDTO timetableUnit) throws WitcurveException {
        log.debug("REST request to update timeTableUnit by id : {}", timetableUnit);
        TimeTableUnitDTO result = timeTableUnitService.saveOrUpdate(timetableUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("timeTableUnit", result.getId().toString()))
            .body(result);
    }
}
