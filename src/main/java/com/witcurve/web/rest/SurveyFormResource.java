package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.service.SurveyFormService;
import com.witcurve.service.SurveySectionService;
import com.witcurve.service.dto.SurveyFormDTO;
import com.witcurve.service.dto.SurveySectionDTO;
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
public class SurveyFormResource {

    private final Logger log = LoggerFactory.getLogger(SurveyFormResource.class);

    @Autowired
    SurveyFormService surveyFormService;

    @Autowired
    SurveySectionService surveySectionService;

    /**
     * creates a surveyForm
     *
     * @param surveyFormDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/survey-forms")
    @Timed
    public ResponseEntity<SurveyFormDTO> createSurveyForm(@RequestBody @Valid SurveyFormDTO surveyFormDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save surveyForm");
        if (surveyFormDTO.getId() != null) {
            throw new WitcurveException("New SurveyForm can't already have an id");
        }
        try {
            SurveyFormDTO result = surveyFormService.saveOrUpdate(surveyFormDTO);
            return ResponseEntity.created(new URI("/api/survey-forms/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("surveyForm", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * creates a surveySection
     *
     * @param surveySectionDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/survey-forms/survey-sections")
    @Timed
    public ResponseEntity<SurveySectionDTO> createSurveySection(@RequestBody @Valid SurveySectionDTO surveySectionDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save surveySection");
        if (surveySectionDTO.getId() != null) {
            throw new WitcurveException("New SurveySection can't already have an id");
        }
        try {
            SurveySectionDTO result = surveySectionService.saveOrUpdate(surveySectionDTO);
            return ResponseEntity.created(new URI("/api/survey-forms/survey-sections" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("surveySection", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * update the given surveyForm
     *
     * @param surveyFormDTO
     * @return
     * @throws WitcurveException
     */
    @PutMapping("/survey-forms")
    @Timed
    public ResponseEntity<SurveyFormDTO> updateSurveyForm(@RequestBody @Valid SurveyFormDTO surveyFormDTO) throws WitcurveException {
        log.debug("Request to update surveyForm");
        if (surveyFormDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        try {
            SurveyFormDTO result = surveyFormService.saveOrUpdate(surveyFormDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("surveyForm", surveyFormDTO.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * update surveyForm status by id with status
     *
     * @param surveyFormId
     * @return
     * @throws WitcurveException
     */
    @PatchMapping("/survey-forms/{surveyFormId}")
    @Timed
    public ResponseEntity<SurveyFormDTO> updateSurveyFormStatus(@PathVariable("surveyFormId") Long surveyFormId, @RequestParam SurveyFormStatus status) {
        log.debug("Request to change status of surveyForm");
        SurveyFormDTO result = surveyFormService.updateSurveyFormStatus(surveyFormId, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update a surveySection
     *
     * @param surveySectionDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/survey-forms/survey-sections")
    @Timed
    public ResponseEntity<SurveySectionDTO> updateSurveySection(@RequestBody @Valid SurveySectionDTO surveySectionDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request to update save surveySection");
        if (surveySectionDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        try {
            SurveySectionDTO result = surveySectionService.saveOrUpdate(surveySectionDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("surveySection", surveySectionDTO.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get surveyForm by id
     *
     * @param surveyFormId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/survey-forms/{surveyFormId}")
    @Timed
    public ResponseEntity<SurveyFormDTO> getSurveyFormById(@PathVariable("surveyFormId") Long surveyFormId) throws WitcurveException {
        log.debug("Request to get SurveyForm with id {}", surveyFormId);
        SurveyFormDTO result = surveyFormService.getOne(surveyFormId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get surveyForms by schoolInfoId
     *
     * @param schoolInfoId
     * @param creator,
     * @param statusList
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/survey-forms/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<SurveyFormDTO>> getSurveyFormBySchoolInfo(@PathVariable("schoolInfoId") Long schoolInfoId,
                                                                         @RequestParam SurveyFormCreator creator,
                                                                         @RequestParam(required = false) List<SurveyFormStatus> statusList) throws WitcurveException {
        log.debug("Request to get SurveyForm with schoolInfo with id : {}, by creator : {} and of statuses : {}", schoolInfoId, creator, statusList);
        List<SurveyFormDTO> result = surveyFormService.findAll(schoolInfoId, creator, statusList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get surveyForms by studentId
     *
     * @param studentId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/survey-forms/students/{studentId}")
    @Timed
    public ResponseEntity<List<SurveyFormDTO>> getSurveyFormByForStudent(@PathVariable("studentId") Long studentId) throws WitcurveException {
        log.debug("Request to get SurveyForm for student with id : {}", studentId);
        List<SurveyFormDTO> result = surveyFormService.findSurveyFormsForStudentId(studentId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get surveyForms by staffId
     *
     * @param staffId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/survey-forms/staff/{staffId}")
    @Timed
    public ResponseEntity<List<SurveyFormDTO>> getSurveyFormByForStaff(@PathVariable("staffId") Long staffId) throws WitcurveException {
        log.debug("Request to get SurveyForm for staff with id : {}", staffId);
        List<SurveyFormDTO> result = surveyFormService.findSurveyFormsForStaffId(staffId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete the surveyForm
     *
     * @param surveyFormId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/survey-forms/{surveyFormId}")
    public ResponseEntity<Void> deleteSurveyFromById(@PathVariable Long surveyFormId) throws WitcurveException, URISyntaxException {
        log.debug("Request to delete surveyForm with id : " + surveyFormId);
        surveyFormService.deleteOne(surveyFormId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("A survey form is deleted with identifier " + surveyFormId,
            surveyFormId.toString())).build();
    }

    /**
     * delete the surveySection
     *
     * @param surveySectionId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/survey-forms/survey-sections/{surveySectionId}")
    public ResponseEntity<Void> deleteSurveySectionById(@PathVariable Long surveySectionId) throws WitcurveException, URISyntaxException {
        log.debug("Request to delete surveySection with id : " + surveySectionId);
        surveySectionService.deleteOne(surveySectionId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("A survey section is deleted with identifier " + surveySectionId,
            surveySectionId.toString())).build();
    }
}
