package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SlotService;
import com.witcurve.service.dto.SlotDTO;
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
public class SlotResource {

    private final static Logger log = LoggerFactory.getLogger(SlotResource.class);

    @Autowired
    SlotService slotService;

    @PostMapping("/slot")
    @Timed
    public ResponseEntity<SlotDTO> createSlot(@RequestBody @Valid SlotDTO slotDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to save slot", slotDTO);
        if (slotDTO.getId() != null) {
            throw new WitcurveException("New Slot cant be id");
        }
        SlotDTO result =slotService.saveOrUpdate(slotDTO);
        return ResponseEntity.created(new URI("/api/slot/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("slot", result.getId().toString()))
            .body(result);
    }

    @PutMapping("/slot")
    @Timed
    public ResponseEntity<SlotDTO> updateSlot(@RequestBody @Valid SlotDTO slotDTO) throws WitcurveException {
        log.debug("Request to update slot: ", slotDTO);
        if (slotDTO.getId() == null) {
            throw new WitcurveException("Update request should have id");
        }
        SlotDTO result =slotService.saveOrUpdate(slotDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("slot", result.getId().toString()))
            .body(result);
    }

    @GetMapping("/slot/{slotId}")
    @Timed
    public ResponseEntity<SlotDTO> getSlot(@PathVariable Long slotId) throws WitcurveException {
        log.debug("Request to get slot with id {}: ", slotId);
        SlotDTO slotDTO = slotService.getSlotById(slotId);
        return new ResponseEntity<>(slotDTO, HttpStatus.OK);
    }


}
