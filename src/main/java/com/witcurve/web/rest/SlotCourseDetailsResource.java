package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SlotCourseDetailsService;
import com.witcurve.service.dto.SlotCourseDetailsDTO;
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
        try {
            SlotCourseDetailsDTO result = slotCourseDetailsService.saveOrUpdate(slotCourseDetailsDTO);
            return ResponseEntity.created(new URI("/api/slot-course-details/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("slotCourseDetails", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("scd_slot_day_UK")) {
                throw new WitcurveException("Unique constraint (gsd_id, day_of_week) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }

        }
    }

    /**
     * updates a slotCourseDetails
     *
     * @param slotCourseDetailsDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/slot-course-details")
    @Timed
    public ResponseEntity<SlotCourseDetailsDTO> udpateSlotCourseDetails(@RequestBody @Valid SlotCourseDetailsDTO slotCourseDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save slotCourseDetails");
        if (slotCourseDetailsDTO.getId() == null) {
            throw new WitcurveException("An update request for slotCourseDetails must have an id");
        }
        try {
            SlotCourseDetailsDTO result = slotCourseDetailsService.saveOrUpdate(slotCourseDetailsDTO);
            return ResponseEntity.created(new URI("/api/slot-course-details/" + result.getId()))
                .headers(HeaderUtil.createEntityUpdateAlert("slotCourseDetails", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("scd_slot_day_UK")) {
                throw new WitcurveException("Unique constraint (gsd_id, day_of_week) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }

        }
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
     * delete the slotCourseDetails
     * @param slotCourseDetailsId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/slot-course-details/{slotCourseDetailsId}")
    @Timed
    public ResponseEntity<Void> deleteSlotCourseDetails(@PathVariable Long slotCourseDetailsId) throws WitcurveException {
        log.debug("REST request to delete SlotCourseDetails: {}", slotCourseDetailsId);
        try {
            slotCourseDetailsService.deleteSlotCourseDetails(slotCourseDetailsId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("A slotCourseDetails is deleted with identifier " + slotCourseDetailsId,
                slotCourseDetailsId.toString())).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get slotCourseDetails by standard id
     *
     * @param standardId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/slot-course-details/standards/{standardId}")
    @Timed
    public ResponseEntity<List<SlotCourseDetailsDTO>> getSlotCourseDetailsByStandardId(@PathVariable("standardId") Long standardId) throws WitcurveException {
        log.debug("Request to get SlotCourseDetails with standard id {}", standardId);
        List<SlotCourseDetailsDTO> result = slotCourseDetailsService.getSlotCourseDetailsByStandardId(standardId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get slotCourseDetails by teacher id
     *
     * @param teacherId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/slot-course-details/teachers/{teacherId}")
    @Timed
    public ResponseEntity<List<SlotCourseDetailsDTO>> getSlotCourseDetailsByTeacherId(@PathVariable("teacherId") Long teacherId) {
        log.debug("Request to get SlotCourseDetails with teacher id : {} ", teacherId);
        List<SlotCourseDetailsDTO> result = slotCourseDetailsService.getSlotCourseDetailsByTeacherId(teacherId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
