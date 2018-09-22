package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.TermService;
import com.witcurve.service.dto.TermDTO;
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
public class TermResource {

    private final Logger log = LoggerFactory.getLogger(TermResource.class);

    @Autowired
    TermService termService;

    /**
     * creates terms
     * @param termDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/terms")
    @Timed
    public ResponseEntity<List<TermDTO>> saveOrUpdate(@RequestBody @Valid List<TermDTO> termDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request to save Terms");
        List<TermDTO> result = termService.saveOrUpdate(termDTOs);
        return ResponseEntity.created(new URI("/api/terms/"))
            .headers(HeaderUtil.createEntityCreationAlert("terms", null))
            .body(result);
    }

    /**
     * get term by id
     * @param termId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/term/{termId}")
    @Timed
    public ResponseEntity<TermDTO> getTermById(@PathVariable("termId") Long termId) throws WitcurveException {
        log.debug("Request to get Term with id {}", termId);
        TermDTO result = termService.getTermById(termId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the term
     * @param termId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/term/{termId}")
    @Timed
    public ResponseEntity<Void> deleteTerm(@PathVariable Long termId) throws WitcurveException {
        log.debug("REST request to delete Term: {}", termId);
        termService.deleteTerm(termId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A term is deleted with identifier " + termId,
            termId.toString())).build();
    }
}
