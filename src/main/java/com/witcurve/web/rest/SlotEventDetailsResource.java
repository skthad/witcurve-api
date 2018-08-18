package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SlotEventDetailsService;
import com.witcurve.service.dto.SlotEventDetailsDTO;
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
public class SlotEventDetailsResource {


    private final Logger log = LoggerFactory.getLogger(SlotEventDetailsResource.class);

    @Autowired
    SlotEventDetailsService slotEventDetailsService;

    /**
     * creates a slotEventDetails
     *
     * @param slotEventDetailsDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/slot-event-details")
    @Timed
    public ResponseEntity<SlotEventDetailsDTO> createSlotEventDetails(@RequestBody @Valid SlotEventDetailsDTO slotEventDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save slotEventDetails");
        if (slotEventDetailsDTO.getId() != null) {
            throw new WitcurveException("New slotEventDetails can't already have an id");
        }
        SlotEventDetailsDTO result = slotEventDetailsService.saveOrUpdate(slotEventDetailsDTO);
        return ResponseEntity.created(new URI("/api/slot-event-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("slotEventDetails", result.getId().toString()))
            .body(result);
    }

    /**
     * get slotEventDetails by id
     *
     * @param slotEventDetailsId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/slot-event-details/{slotEventDetailsId}")
    @Timed
    public ResponseEntity<SlotEventDetailsDTO> getSlotEventDetailsById(@PathVariable("slotEventDetailsId") Long slotEventDetailsId) throws WitcurveException {
        log.debug("Request to get SlotEventDetails with id {}", slotEventDetailsId);
        SlotEventDetailsDTO result = slotEventDetailsService.getSlotEventDetailsById(slotEventDetailsId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given slotEventDetails
     *
     * @param slotEventDetailsDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/slot-event-details")
    @Timed
    public ResponseEntity<SlotEventDetailsDTO> updateSlotEventDetails(@RequestBody @Valid SlotEventDetailsDTO slotEventDetailsDTO) throws WitcurveException {
        log.debug("Request to update slotEventDetails");
        if (slotEventDetailsDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        SlotEventDetailsDTO result = slotEventDetailsService.saveOrUpdate(slotEventDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("slotEventDetails", slotEventDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the slotEventDetails
     * @param slotEventDetailsId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/slot-event-details/{slotEventDetailsId}")
    @Timed
    public ResponseEntity<Void> deleteSlotEventDetails(@PathVariable Long slotEventDetailsId) throws WitcurveException {
        log.debug("REST request to delete SlotEventDetails: {}", slotEventDetailsId);
        slotEventDetailsService.deleteSlotEventDetails(slotEventDetailsId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A slotEventDetails is deleted with identifier " + slotEventDetailsId,
            slotEventDetailsId.toString())).build();
    }

}
