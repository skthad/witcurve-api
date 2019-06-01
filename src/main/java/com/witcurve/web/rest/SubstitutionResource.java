package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SubstitutionService;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.service.dto.SubstitutionDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SubstitutionResource {

    private final Logger log = LoggerFactory.getLogger(SubstitutionResource.class);

    @Autowired
    SubstitutionService substitutionService;

    /**
     * get Staff by id
     * @return
     * @throws WitcurveException
     */

    @PostMapping("/substitutions")
    @Timed
    public ResponseEntity<SubstitutionDTO> substituteTeacher(@RequestBody SubstitutionDTO substitutionDTO) throws WitcurveException {
        log.debug("Request to substitute");

        try {
            SubstitutionDTO result = substitutionService.substitute(substitutionDTO);
            return ResponseEntity.ok(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("scd_date_id_UK")) {
                throw new WitcurveException("Unique constraint (scd_id, date) violated");
            } else if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get substitute teacher suggestions
     * @param scdId
     * @param date
     *
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/substitutions/suggestions")
    @Timed
    public ResponseEntity<List<StaffDTO>> getStaffListForSubstituteSuggestion(@RequestParam("scdId") Long scdId,
                                                                              @RequestParam("date") LocalDate date) throws WitcurveException {
        log.debug("Request to get substitute teacher for scdId {} on {}", scdId, date);
        List<StaffDTO> result = substitutionService.getSubstituteSuggestion(scdId, date);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/substitutions")
    @Timed
    public ResponseEntity<List<SubstitutionDTO>> getSubstitutions(@RequestParam("gsdId") List<Long> gsdIds,
                                                                  @RequestParam("date") LocalDate date) throws WitcurveException {
        log.debug("Request to get substitutions on {}", date);
        List<SubstitutionDTO> result = substitutionService.getSubstitutions(gsdIds, date);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
