package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SurveySubmissionService;
import com.witcurve.service.dto.SurveySubmissionDTO;
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
public class SurveySubmissionResource {

    private final Logger log = LoggerFactory.getLogger(SurveySubmissionResource.class);

    @Autowired
    SurveySubmissionService surveySubmissionService;

    /**
     * creates a surveySubmission
     *
     * @param surveySubmissionDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/survey-submissions")
    @Timed
    public ResponseEntity<SurveySubmissionDTO> createSurveyAnswer(@RequestBody @Valid SurveySubmissionDTO surveySubmissionDTO) throws URISyntaxException {
        log.debug("Request Save surveySubmission : {} ", surveySubmissionDTO);
        if (surveySubmissionDTO.getId() != null) {
            throw new WitcurveException("New surveySubmissionDTO can't already have an id");
        }
        try {
            SurveySubmissionDTO result = surveySubmissionService.save(surveySubmissionDTO);
            return ResponseEntity.created(new URI("/api/survey-submissions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("surveySubmission", result.getId().toString()))
                .body(result);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else if (e.getMessage().contains("survey_form_user_id")) {
                throw new WitcurveException("User can submit the form only once");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * get surveySubmissions by formId
     *
     * @param surveyFormId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/survey-submissions/survey-forms/{surveyFormId}")
    @Timed
    public ResponseEntity<List<SurveySubmissionDTO>> getSubmissionsByFormId(@PathVariable("surveyFormId") Long surveyFormId) {
        log.debug("Request to get  surveySubmission by formId : {} ", surveyFormId);
        List<SurveySubmissionDTO> result = surveySubmissionService.getByFormId(surveyFormId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get surveySubmissions by userId
     *
     * @param userId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/survey-submissions/user/{userId}")
    @Timed
    public ResponseEntity<List<SurveySubmissionDTO>> getSubmissionsByUserId(@PathVariable Long userId) {
        log.debug("Request to get  surveySubmission by userId : {} ", userId);
        List<SurveySubmissionDTO> result = surveySubmissionService.getByUserId(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
