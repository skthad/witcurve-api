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
        List<GeneralSlotDetailsDTO> result = generalSlotDetailsService.saveOrUpdate(generalSlotDetailsDTOs);
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
     * update the given generalSlotDetails
     *
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/general-slot-details/clone")
    @Timed
    public ResponseEntity<Void> cloneGeneralSlotDetails(@RequestParam("sourceClassId") Long sourceClassId,
                                                                         @RequestParam("destinationClassIds") List<Long> destinationClassIds) throws WitcurveException {
        log.debug("Request to clone generalSlotDetails");
        generalSlotDetailsService.clone(sourceClassId, destinationClassIds);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "General slot details cloned", null)).build();
    }

    /**
     * delete the generalSlotDetails
     * @param generalSlotDetailsId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/general-slot-details/{generalSlotDetailsId}")
    @Timed
    public ResponseEntity<Void> deleteGeneralSlotDetails(@PathVariable Long generalSlotDetailsId) throws WitcurveException {
        log.debug("REST request to delete GeneralSlotDetails: {}", generalSlotDetailsId);
        generalSlotDetailsService.deleteGeneralSlotDetails(generalSlotDetailsId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A generalSlotDetails is deleted with identifier " + generalSlotDetailsId,
            generalSlotDetailsId.toString())).build();
    }

    /**
     * get generalSlotDetails by class id
     *
     * @param classId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/general-slot-details/class/{classId}")
    @Timed
    public ResponseEntity<List<GeneralSlotDetailsDTO>> getGeneralSlotDetailsByClassId(@PathVariable("classId") Long classId) throws WitcurveException {
        log.debug("Request to get GeneralSlotDetails with class id {}", classId);
        List<GeneralSlotDetailsDTO> result = generalSlotDetailsService.getGeneralSlotDetailsByClassId(classId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
