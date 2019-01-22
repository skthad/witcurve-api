package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.GSDStatus;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.GeneralSlotDetailsService;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
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
    public ResponseEntity<List<GeneralSlotDetailsDTO>> createGeneralSlotDetails(@RequestParam(required = false, defaultValue = "false") Boolean exam,
                                                                                @RequestBody @Valid List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) throws WitcurveException, URISyntaxException {
        log.debug("Request Save generalSlotDetails");
        List<GeneralSlotDetailsDTO> result;
        try {
            if (Boolean.TRUE.equals(exam)) {
                log.debug("Request Save exam slots");
                result = generalSlotDetailsService.createExamSlots(generalSlotDetailsDTOs);
            } else {
                log.debug("Request Save generalSlotDetails");
                result = generalSlotDetailsService.createGSDs(generalSlotDetailsDTOs);
            }
            return ResponseEntity.created(new URI("/api/general-slot-details/"))
                .headers(HeaderUtil.createEntityCreationAlert("generalSlotDetails", null))
                .body(result);
        } catch (Exception e) {
            if (e.getMessage().contains("Could not commit JPA transaction")) {
                throw new WitcurveException("Some data validation failed..." + e.getMessage());
            } else {
                throw new WitcurveException("Error occurred: " + e.getMessage());
            }

        }
    }

    /**
     * get generalSlotDetails by standard id
     *
     * @param standardId
     * @param status
     *
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/general-slot-details/standard/{standardId}")
    @Timed
    public ResponseEntity<List<GeneralSlotDetailsDTO>> getGeneralSlotDetailsByStandardId(@PathVariable("standardId") Long standardId,
                                                                                         @RequestParam(value = "status", required = false) GSDStatus status) throws WitcurveException {
        log.debug("Request to get GeneralSlotDetails with standard id {}", standardId);
        List<GeneralSlotDetailsDTO> result = generalSlotDetailsService.getGeneralSlotDetailsByStandardId(standardId, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get generalSlotDetails by standard id
     *
     * @param grade
     * @param status
     * @param examId
     *
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/general-slot-details/grade/{grade}")
    @Timed
    public ResponseEntity<List<GeneralSlotDetailsDTO>> getExamSlotsByGrade(@PathVariable("grade") Grade grade,
                                                                           @RequestParam(value = "examId", required = false) Long examId,
                                                                           @RequestParam(value = "status", required = false) GSDStatus status) throws WitcurveException {
        log.debug("Request to get GeneralSlotDetails with grade {} and examId {} ", grade, examId);
        List<GeneralSlotDetailsDTO> result = generalSlotDetailsService.getExamSlotsByGrade(grade, examId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/general-slot-details/deactivate")
    @Timed
    public ResponseEntity<Void> deactivateGeneralSlotDetails(@RequestParam("standardIds") List<Long> standardIds) throws WitcurveException {

        log.debug("Request to deactivate generalSlotDetails");
        if (standardIds.size() == 0) {
            throw new WitcurveException("list of standard IDs must be provided to deactivate GSDs");
        }
        generalSlotDetailsService.deactivateGSDsForStandards(standardIds);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert( "General slot details deactivated", null)).build();
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
                                                           @RequestParam("bindingId") String bindingId) {
        log.debug("Request to activate generalSlotDetails");
        generalSlotDetailsService.activateGSDsForStandard(standardId, bindingId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert( "General slot details activated", null)).build();
    }

    /**
     * clone the given generalSlotDetails
     *
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/general-slot-details/clone")
    @Timed
    public ResponseEntity<Void> cloneGeneralSlotDetails(@RequestParam(value = "sourceStandardId", required = false) Long sourceStandardId,
                                                        @RequestParam(value = "destinationStandardIds", required = false) List<Long> destinationStandardIds,
                                                        @RequestParam(value = "sourceGrade", required = false) Grade sourceGrade,
                                                        @RequestParam(value = "destinationGrades", required = false) List<Grade> destinationGrades,
                                                        @RequestParam(value = "examId", required = false) Long examId) throws WitcurveException {
        log.debug("Request to clone generalSlotDetails");

        if (examId != null) {
            log.debug("Request to clone exam slots");
            if (sourceGrade == null || destinationGrades == null || destinationGrades.size() == 0) {
                throw new WitcurveException("source and destination grade(s) are mandatory to clone exam slots");
            }
            generalSlotDetailsService.cloneExamSlots(sourceGrade, destinationGrades, examId);
        } else {
            log.debug("Request to clone generalSlotDetails");
            if (sourceStandardId == null || destinationStandardIds == null || destinationStandardIds.size() == 0) {
                throw new WitcurveException("source and destination standard ID(s) are mandatory to clone GSDs");
            }
            generalSlotDetailsService.cloneGSDs(sourceStandardId, destinationStandardIds);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "General slot details cloned", null)).build();
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
     * delete the gsd
     * @param bindingId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/general-slot-details")
    @Timed
    public ResponseEntity<Void> deleteSlots(@RequestParam String bindingId) throws WitcurveException {
        log.debug("REST request to delete GeneralSlotDetails for bindingId: {}", bindingId);
        try {
            generalSlotDetailsService.deleteGSDsByBindingId(bindingId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("GSD deleted with binding Id " + bindingId,
                bindingId)).build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key constraint might have failed while deleting");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * delete the gsd
     * @param examId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/general-slot-details/grade/{grade}")
    @Timed
    public ResponseEntity<Void> deleteExamSlotsByGradeExamId(@PathVariable Grade grade, @RequestParam Long examId) throws WitcurveException {
        log.debug("REST request to delete Exam Slots for grade: {} and examId: {}", grade, examId);
        generalSlotDetailsService.deleteExamSlotsByGradeAndExamId(grade, examId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("Exam Slots deleted with grade " + grade + " and examId " + examId,
            grade.toString())).build();
    }


}
