package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.SessionFeeStructureService;
import com.witcurve.service.dto.SessionFeeStructureDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
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
public class SessionFeeStructureResource {

    private final Logger log = LoggerFactory.getLogger(SessionFeeStructureResource.class);

    @Autowired
    SessionFeeStructureService sessionFeeStructureService;

    /**
     * creates and updates sessionFeeStructures
     *
     * @param sessionFeeStructureDTOs
     * @param grade
     * @param sessionId
     *
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/session-fee-structure")
    @Timed
    public ResponseEntity<List<SessionFeeStructureDTO>> createSessionFeeStructure(@RequestBody @Valid List<SessionFeeStructureDTO> sessionFeeStructureDTOs, @RequestParam Grade grade, @RequestParam Long sessionId) throws WitcurveException, URISyntaxException {
        log.debug("Request to save or update sessionFeeStructureList : {} for grade : {} and session with id : {}", sessionFeeStructureDTOs, grade, sessionId);
        List<SessionFeeStructureDTO> result = sessionFeeStructureService.saveOrUpdate(sessionFeeStructureDTOs, grade, sessionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * get sessionFeeStructure
     *
     * @param sessionFeeStructureId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */

    @GetMapping("/session-fee-structure/{sessionFeeStructureId}")
    @Timed
    public ResponseEntity<SessionFeeStructureDTO> getById(@PathVariable Long sessionFeeStructureId) throws WitcurveException {
        log.debug("Request to get sessionFeeStructure by id {}", sessionFeeStructureId);
        SessionFeeStructureDTO result = sessionFeeStructureService.getById(sessionFeeStructureId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get list of sessionFeeStructure
     *
     * @param grade
     * @param schoolInfoId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/session-fee-structure/school-info/{schoolInfoId}/grade/{grade}")
    @Timed
    public ResponseEntity<List<SessionFeeStructureDTO>> getBySchoolInfoAndGrade(@PathVariable Long schoolInfoId, @PathVariable Grade grade) throws WitcurveException {
        log.debug("Request to get sessionFeeStructure by schoolInfoId :{} and grade :{}", schoolInfoId, grade);
        List<SessionFeeStructureDTO> result = sessionFeeStructureService.getBySchoolInfoIdAndGrade(schoolInfoId, grade);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get list of sessionFeeStructure
     *
     * @param grade
     * @param sessionId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */

    @GetMapping("/session-fee-structure/academic-session/{sessionId}/grade/{grade}")
    @Timed
    public ResponseEntity<List<SessionFeeStructureDTO>> getBySessionIdAndGrade(@PathVariable Long sessionId, @PathVariable Grade grade) throws WitcurveException {
        log.debug("Request to get sessionFeeStructure by sessionId :{} and grade :{}", sessionId, grade);
        List<SessionFeeStructureDTO> result = sessionFeeStructureService.getBySessionIdAndGrade(sessionId,grade);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/session-fee-structure/{sessionFeeStructureId}")
    @Timed
    public ResponseEntity<Void> deleteSessionFieldStructure(@PathVariable Long sessionFeeStructureId) throws WitcurveException {
        log.debug("Request to get sessionFeeStructure by id {}", sessionFeeStructureId);

        sessionFeeStructureService.deleteById(sessionFeeStructureId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(" SessionFeeStructure is deleted with identifier " + sessionFeeStructureId,
            sessionFeeStructureId.toString())).build();
    }
}
