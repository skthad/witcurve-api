package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SubstitutionService;
import com.witcurve.service.dto.SlotCourseDetailsDTO;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.service.dto.SubstitutionDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SubstitutionResource {

    private final Logger log = LoggerFactory.getLogger(SubstitutionResource.class);

    @Autowired
    SubstitutionService substitutionService;

    /**
     * get substitute teacher suggestions
     * @param gsdId
     * @param teacherId
     * @param date
     *
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/substitution")
    @Timed
    public ResponseEntity<HashMap<Long, List<SlotCourseDetailsDTO>>> getStaffListForSubstituteSuggestion(@RequestParam("gsdId") Long gsdId,
                                                                                                         @RequestParam("teacherId") Long teacherId,
                                                                                                         @RequestParam("date") LocalDate date) throws WitcurveException {
        log.debug("Request to get substitute teacher for {} on {}", teacherId, date);
        HashMap<Long, List<SlotCourseDetailsDTO>> result = substitutionService.getSubstituteSuggestion(gsdId, teacherId, date);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get Staff by id
     * @return
     * @throws WitcurveException
     */

    @PostMapping("/substitution")
    @Timed
    public ResponseEntity<SubstitutionDTO> substituteTeacher(@RequestBody SubstitutionDTO substitutionDTO) throws WitcurveException {
        log.debug("Request to substitute");
        SubstitutionDTO result = substitutionService.substitute(substitutionDTO);
        return ResponseEntity.ok(result);
    }
}
