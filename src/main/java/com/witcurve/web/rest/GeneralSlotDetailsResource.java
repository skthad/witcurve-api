package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.GeneralSlotDetailsService;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
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
public class GeneralSlotDetailsResource {

    private final Logger log = LoggerFactory.getLogger(GeneralSlotDetailsResource.class);

    @Autowired
    GeneralSlotDetailsService generalSlotDetailsService;

    /**
     * creates a generalSlotDetails
     *
     * @param generalSlotDetailsDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/general-slot-details")
    @Timed
    public ResponseEntity<GeneralSlotDetailsDTO> createGeneralSlotDetails(@RequestBody @Valid GeneralSlotDetailsDTO generalSlotDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save generalSlotDetails");
        if (generalSlotDetailsDTO.getId() != null) {
            throw new WitcurveException("New generalSlotDetails can't already have an id");
        }
        GeneralSlotDetailsDTO result = generalSlotDetailsService.saveOrUpdate(generalSlotDetailsDTO);
        return ResponseEntity.created(new URI("/api/general-slot-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("generalSlotDetails", result.getId().toString()))
            .body(result);
    }

    /**
     * get generalSlotDetails by id
     *
     * @param generalSlotDetailsId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/general-slot-details/{generalSlotDetailsId}")
    @Timed
    public ResponseEntity<GeneralSlotDetailsDTO> getGeneralSlotDetailsById(@PathVariable("generalSlotDetailsId") Long generalSlotDetailsId) throws WitcurveException {
        log.debug("Request to get GeneralSlotDetails with id {}", generalSlotDetailsId);
        GeneralSlotDetailsDTO result = generalSlotDetailsService.getGeneralSlotDetailsById(generalSlotDetailsId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given generalSlotDetails
     *
     * @param generalSlotDetailsDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/general-slot-details")
    @Timed
    public ResponseEntity<GeneralSlotDetailsDTO> updateGeneralSlotDetails(@RequestBody @Valid GeneralSlotDetailsDTO generalSlotDetailsDTO) throws WitcurveException {
        log.debug("Request to update generalSlotDetails");
        if (generalSlotDetailsDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        GeneralSlotDetailsDTO result = generalSlotDetailsService.saveOrUpdate(generalSlotDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("generalSlotDetails", generalSlotDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the generalSlotDetails
     * @param generalSlotDetailsId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/academic-session/{generalSlotDetailsId}")
    @Timed
    public ResponseEntity<Void> deleteGeneralSlotDetails(@PathVariable Long generalSlotDetailsId) throws WitcurveException {
        log.debug("REST request to delete GeneralSlotDetails: {}", generalSlotDetailsId);
        generalSlotDetailsService.deleteGeneralSlotDetails(generalSlotDetailsId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A generalSlotDetails is deleted with identifier " + generalSlotDetailsId,
            generalSlotDetailsId.toString())).build();
    }


}
