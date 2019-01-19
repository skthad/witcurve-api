package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.TermService;
import com.witcurve.service.dto.TermDTO;
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
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TermResource {

    private final Logger log = LoggerFactory.getLogger(TermResource.class);

    @Autowired
    TermService termService;

    /**
     * creates term
     * @param termStartDate
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/terms/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<TermDTO> create(@PathVariable Long schoolInfoId,
                                                @RequestParam LocalDate termStartDate) throws WitcurveException, URISyntaxException {
        log.debug("Request to create Term for schoolInfoId: {} with start date: {}", schoolInfoId, termStartDate);

        try {
            TermDTO result = termService.createTerm(schoolInfoId, termStartDate);
            return ResponseEntity.created(new URI("/api/terms"))
                .headers(HeaderUtil.createEntityCreationAlert("terms", null))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            throw new WitcurveException("DataIntegrityViolationException occurred.");
        }

    }

    /**
     * updates term
     * @param termDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/terms")
    @Timed
    public ResponseEntity<TermDTO> update(@RequestBody @Valid TermDTO termDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to update Term with id {}", termDTO.getId());

        try {
            TermDTO result = termService.updateTerm(termDTO);
            return ResponseEntity.created(new URI("/api/terms"))
                .headers(HeaderUtil.createEntityUpdateAlert("terms", null))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            throw new WitcurveException("DataIntegrityViolationException occurred.");
        }

    }

    /**
     * get term by id
     * @param termId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/terms/{termId}")
    @Timed
    public ResponseEntity<TermDTO> getTermById(@PathVariable("termId") Long termId) throws WitcurveException {
        log.debug("Request to get Term with id {}", termId);
        TermDTO result = termService.getTermById(termId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get terms by session id
     * @param sessionId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/terms/academic-session/{sessionId}")
    @Timed
    public ResponseEntity<List<TermDTO>> getTermsByAcademicSessionId(@PathVariable("sessionId") Long sessionId) throws WitcurveException {
        log.debug("Request to get Terms with academic session id {}", sessionId);
        List<TermDTO> result = termService.getTermsByAcademicSessionId(sessionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the term
     * @param termId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/terms/{termId}")
    @Timed
    public ResponseEntity<Void> deleteTerm(@PathVariable Long termId) throws WitcurveException {
        log.debug("REST request to delete Term: {}", termId);
        termService.deleteTerm(termId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A term is deleted with identifier " + termId,
            termId.toString())).build();
    }
}
