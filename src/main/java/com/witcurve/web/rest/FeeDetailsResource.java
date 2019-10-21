package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.FeeDetailsType;
import com.witcurve.service.FeeDetailsService;
import com.witcurve.service.dto.FeeDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FeeDetailsResource {

    private final Logger log = LoggerFactory.getLogger(FeeDetailsResource.class);

    @Autowired
    FeeDetailsService feeDetailsService;

    /**
     * creates and updates feeDetails
     *
     * @param feeDetailsDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/fee-details")
    @Timed
    public ResponseEntity<FeeDetailsDTO> createFeeDetails(@RequestBody @Valid FeeDetailsDTO feeDetailsDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to save FeeDetails {}", feeDetailsDTO);
        FeeDetailsDTO result = feeDetailsService.saveOrUpdateFeeDetails(feeDetailsDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get feeDetails for schholInfoId
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/fee-details/school-Info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<FeeDetailsDTO>> getFeeDetailsBySchoolInfoId(@PathVariable Long schoolInfoId, @RequestParam(required = false) FeeDetailsType type) throws WitcurveException {
        log.debug("Request to get FeeDetails for school info with id : {} of feeDetailsType : {} ", schoolInfoId, type);
        List<FeeDetailsDTO> result = feeDetailsService.getFeeDetailsBySchoolInfoId(schoolInfoId, type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get feeDetails by id
     * @param feeDetailsId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/fee-details/{feeDetailsId}")
    @Timed
    public ResponseEntity<FeeDetailsDTO> getFeeDetailsById(@PathVariable Long feeDetailsId) throws WitcurveException {
        log.debug("Request to get FeeDetails for Id {}", feeDetailsId);
        FeeDetailsDTO result = feeDetailsService.getFeeDetailsById(feeDetailsId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the feeDetails
     * @param feeDetailsId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/fee-details/{feeDetailsId}")
    @Timed
    public ResponseEntity<Void> deleteFeeDetails(@PathVariable Long feeDetailsId) throws WitcurveException {
        log.debug("REST request to delete FeeDetails: {}", feeDetailsId);
        feeDetailsService.deleteFeeDetails(feeDetailsId);
    }
}
