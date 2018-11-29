package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.GSDStatus;
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
import java.util.List;

@RestController
@RequestMapping("/api")
public class GeneralSlotDetailsResource {

    private final Logger log = LoggerFactory.getLogger(GeneralSlotDetailsResource.class);

    @Autowired
    GeneralSlotDetailsService generalSlotDetailsService;

    /**
     * creates a generalSlotDetails
     *
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/general-slot-details")
    @Timed
    public ResponseEntity<List<GeneralSlotDetailsDTO>> createGeneralSlotDetails(@RequestBody @Valid List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request Save generalSlotDetails");
        List<GeneralSlotDetailsDTO> result = generalSlotDetailsService.create(generalSlotDetailsDTOs);
        return ResponseEntity.created(new URI("/api/general-slot-details/"))
            .headers(HeaderUtil.createEntityCreationAlert("generalSlotDetails", null))
            .body(result);
    }

    /**
     * updates a generalSlotDetails
     *
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/general-slot-details")
    @Timed
    public ResponseEntity<List<GeneralSlotDetailsDTO>> updateGeneralSlotDetails(@RequestBody @Valid List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request Save generalSlotDetails");
        List<GeneralSlotDetailsDTO> result = generalSlotDetailsService.update(generalSlotDetailsDTOs);
        return ResponseEntity.created(new URI("/api/general-slot-details/"))
            .headers(HeaderUtil.createEntityCreationAlert("generalSlotDetails", null))
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
     * deactivate generalSlotDetails for a standard list
     *
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/general-slot-details/activate")
    @Timed
    public ResponseEntity<Void> activateGeneralSlotDetails(@RequestParam("standardId") Long standardId,
                                                             @RequestParam("bindingId") String bindingId) throws WitcurveException {
        log.debug("Request to activate generalSlotDetails");
        generalSlotDetailsService.activate(standardId, bindingId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert( "General slot details activated", null)).build();
    }

    @PutMapping("/general-slot-details/deactivate")
    @Timed
    public ResponseEntity<Void> deactivateGeneralSlotDetails(@RequestParam("standardIds") List<Long> standardIds) throws WitcurveException {
        log.debug("Request to deactivate generalSlotDetails");
        generalSlotDetailsService.deactivate(standardIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert( "General slot details deactivated", null)).build();
    }

    /**
     * update the given generalSlotDetails
     *
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/general-slot-details/clone")
    @Timed
    public ResponseEntity<Void> cloneGeneralSlotDetails(@RequestParam("sourceStandardId") Long sourceStandardId,
                                                        @RequestParam("destinationStandardIds") List<Long> destinationStandardIds) throws WitcurveException {
        log.debug("Request to clone generalSlotDetails");
        generalSlotDetailsService.clone(sourceStandardId, destinationStandardIds);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "General slot details cloned", null)).build();
    }

    /**
     * get generalSlotDetails by standard id
     *
     * @param standardId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/general-slot-details/standard/{standardId}")
    @Timed
    public ResponseEntity<List<GeneralSlotDetailsDTO>> getGeneralSlotDetailsByStandardId(@PathVariable("standardId") Long standardId,
                                                                                         @RequestParam("status") GSDStatus status) throws WitcurveException {
        log.debug("Request to get GeneralSlotDetails with standard id {}", standardId);
        List<GeneralSlotDetailsDTO> result = generalSlotDetailsService.getGeneralSlotDetailsByStandardIdAndStatus(standardId, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the gsd
     * @param bindingId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/general-slot-details")
    @Timed
    public ResponseEntity<Void> deleteStaff(@RequestParam String bindingId) throws WitcurveException {
        log.debug("REST request to delete GeneralSlotDetails for bindingId: {}", bindingId);
        generalSlotDetailsService.deleteByBindingId(bindingId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("GSD was deleted with binding Id " + bindingId,
            bindingId)).build();
    }


}
