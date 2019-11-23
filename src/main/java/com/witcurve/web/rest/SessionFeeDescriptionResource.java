package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SessionFeeDescriptionService;
import com.witcurve.service.dto.SessionFeeDescriptionDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class SessionFeeDescriptionResource {

    private final Logger log = LoggerFactory.getLogger(SessionFeeStructureResource.class);

    @Autowired
    SessionFeeDescriptionService sessionFeeDescriptionService;

    /**
     * creates sessionFeeDescription
     *
     * @param sessionFeeDescriptionDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/session_fee_descriptions")
    @Timed
    public ResponseEntity<SessionFeeDescriptionDTO> createSessionFeeDescription(@RequestBody @Valid SessionFeeDescriptionDTO sessionFeeDescriptionDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to save sessionFeeDescription : {} ", sessionFeeDescriptionDTO);
        if (sessionFeeDescriptionDTO.getId() != null) {
            throw new WitcurveException("New record can not have an id");
        }
        SessionFeeDescriptionDTO result = sessionFeeDescriptionService.saveOrUpdate(sessionFeeDescriptionDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * updates sessionFeeDescription
     *
     * @param sessionFeeDescriptionDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/session_fee_descriptions")
    @Timed
    public ResponseEntity<SessionFeeDescriptionDTO> updateSessionFeeDescription(@RequestBody @Valid SessionFeeDescriptionDTO sessionFeeDescriptionDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to update sessionFeeDescription : {} ", sessionFeeDescriptionDTO);
        if (sessionFeeDescriptionDTO.getId() == null) {
            throw new WitcurveException("Id is require to update the record");
        }
        SessionFeeDescriptionDTO result = sessionFeeDescriptionService.saveOrUpdate(sessionFeeDescriptionDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
