package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SlotCourseDetailsService;
import com.witcurve.service.dto.SlotCourseDetailsDTO;
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
public class SlotCourseDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SlotCourseDetailsResource.class);

    @Autowired
    SlotCourseDetailsService slotCourseDetailsService;

    /**
     * creates a slotCourseDetails
     *
     * @param slotCourseDetailsDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/slot-course-details")
    @Timed
    public ResponseEntity<SlotCourseDetailsDTO> createSlotCourseDetails(@RequestBody @Valid SlotCourseDetailsDTO slotCourseDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save slotCourseDetails");
        if (slotCourseDetailsDTO.getId() != null) {
            throw new WitcurveException("New slotCourseDetails can't already have an id");
        }
        SlotCourseDetailsDTO result = slotCourseDetailsService.saveOrUpdate(slotCourseDetailsDTO);
        return ResponseEntity.created(new URI("/api/slot-course-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("slotCourseDetails", result.getId().toString()))
            .body(result);
    }

    /**
     * get slotCourseDetails by id
     *
     * @param slotCourseDetailsId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/slot-course-details/{slotCourseDetailsId}")
    @Timed
    public ResponseEntity<SlotCourseDetailsDTO> getSlotCourseDetailsById(@PathVariable("slotCourseDetailsId") Long slotCourseDetailsId) throws WitcurveException {
        log.debug("Request to get SlotCourseDetails with id {}", slotCourseDetailsId);
        SlotCourseDetailsDTO result = slotCourseDetailsService.getSlotCourseDetailsById(slotCourseDetailsId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given slotCourseDetails
     *
     * @param slotCourseDetailsDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/slot-course-details")
    @Timed
    public ResponseEntity<SlotCourseDetailsDTO> updateSlotCourseDetails(@RequestBody @Valid SlotCourseDetailsDTO slotCourseDetailsDTO) throws WitcurveException {
        log.debug("Request to update slotCourseDetails");
        if (slotCourseDetailsDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        SlotCourseDetailsDTO result = slotCourseDetailsService.saveOrUpdate(slotCourseDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("slotCourseDetails", slotCourseDetailsDTO.getId().toString()))
            .body(result);
    }



}
