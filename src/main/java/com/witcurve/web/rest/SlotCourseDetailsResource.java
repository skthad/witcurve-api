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
     * @param slotCourseDetailsDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/slot-course-details")
    @Timed
    public ResponseEntity<List<SlotCourseDetailsDTO>> createSlotCourseDetails(@RequestBody @Valid List<SlotCourseDetailsDTO> slotCourseDetailsDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request Save slotCourseDetails");
        List<SlotCourseDetailsDTO> result = slotCourseDetailsService.saveOrUpdate(slotCourseDetailsDTOs);
        return ResponseEntity.created(new URI("/api/slot-course-details/"))
            .headers(HeaderUtil.createEntityUpdateAlert("slotCourseDetails", ""))
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
     * delete the slotCourseDetails
     * @param slotCourseDetailsId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/slot-course-details/{slotCourseDetailsId}")
    @Timed
    public ResponseEntity<Void> deleteSlotCourseDetails(@PathVariable Long slotCourseDetailsId) throws WitcurveException {
        log.debug("REST request to delete SlotCourseDetails: {}", slotCourseDetailsId);
        slotCourseDetailsService.deleteSlotCourseDetails(slotCourseDetailsId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A slotCourseDetails is deleted with identifier " + slotCourseDetailsId,
            slotCourseDetailsId.toString())).build();
    }

    /**
     * get slotCourseDetails by standard id
     *
     * @param standardId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/slot-course-details/standard/{standardId}")
    @Timed
    public ResponseEntity<List<SlotCourseDetailsDTO>> getSlotCourseDetailsByStandardId(@PathVariable("standardId") Long standardId) throws WitcurveException {
        log.debug("Request to get SlotCourseDetails with standard id {}", standardId);
        List<SlotCourseDetailsDTO> result = slotCourseDetailsService.getSlotCourseDetailsByStandardId(standardId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get slotCourseDetails by teacher id and term id
     *
     * @param teacherId
     * @oaram termId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/slot-course-details/teacher/{teacherId}")
    @Timed
    public ResponseEntity<List<SlotCourseDetailsDTO>> getSlotCourseDetailsByTeacherAndTermId(@PathVariable("teacherId") Long teacherId, @RequestParam("termId") Long termId) throws WitcurveException {
        log.debug("Request to get SlotCourseDetails with teacher id : {} and term id : {}", teacherId, termId);
        List<SlotCourseDetailsDTO> result = slotCourseDetailsService.getSlotCourseDetailsByTeacherIdAndTermId(teacherId, termId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
