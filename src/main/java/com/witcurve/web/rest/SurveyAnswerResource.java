package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SurveyAnswerService;
import com.witcurve.service.dto.SurveyAnswerDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SurveyAnswerResource {

    private final Logger log = LoggerFactory.getLogger(SurveyAnswerResource.class);

    @Autowired
    SurveyAnswerService surveyAnswerService;

    /**
     * creates a surveyAnswer
     *
     * @param surveyAnswerDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/survey-answers")
    @Timed
    public ResponseEntity<SurveyAnswerDTO> saveSurveyAnswer(@RequestBody @Valid SurveyAnswerDTO surveyAnswerDTO) throws URISyntaxException {
        log.debug("Request Save surveyAnswer : {} ", surveyAnswerDTO);
        if (surveyAnswerDTO.getId() != null) {
            throw new WitcurveException("New SurveyAnswer can't already have an id");
        }
        try {
            SurveyAnswerDTO result = surveyAnswerService.saveOrUpdate(surveyAnswerDTO);
            return ResponseEntity.created(new URI("/api/survey-answers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("surveyAnswer", result.getId().toString()))
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
     * updates a surveyAnswer
     *
     * @param surveyAnswerDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/survey-answers")
    @Timed
    public ResponseEntity<SurveyAnswerDTO> updateSurveyAnswer(@RequestBody @Valid SurveyAnswerDTO surveyAnswerDTO) throws URISyntaxException {
        log.debug("Request to update surveyAnswer : {} ", surveyAnswerDTO);
        if (surveyAnswerDTO.getId() == null) {
            throw new WitcurveException("Id is require to update SurveyQuestion");
        }
        try {
            SurveyAnswerDTO result = surveyAnswerService.saveOrUpdate(surveyAnswerDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("surveyAnswer", surveyAnswerDTO.getId().toString()))
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
     * get surveyAnswers by formId & userId
     *
     * @param surveyFormId
     * @param userId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/survey-answers/survey-forms/{surveyFormId}/user/{userId}")
    @Timed
    public ResponseEntity<List<SurveyAnswerDTO>> getSurveyAnswerByFormIdAndUserId(@PathVariable("surveyFormId") Long surveyFormId, @PathVariable("userId") Long userId) throws URISyntaxException {
        log.debug("Request to get surveyAnswer by surveyFormId and userId : {} ", surveyFormId, userId);
        List<SurveyAnswerDTO> result = surveyAnswerService.getByFormIdAndUserId(surveyFormId, userId);
        return ResponseEntity.ok(result);
    }

    /**
     * get surveyAnswers by sectionId & userId
     *
     * @param sectionId
     * @param userId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */

    @GetMapping("/survey-answers/survey-sections/{sectionId}/user/{userId}")
    @Timed
    public ResponseEntity<List<SurveyAnswerDTO>> getSurveyAnswerBySectionIdAndUserId(@PathVariable("sectionId") Long sectionId, @PathVariable("userId") Long userId) throws URISyntaxException {
        log.debug("Request to get surveyAnswer by sectionId and userId : {} ", sectionId, userId);
        List<SurveyAnswerDTO> result = surveyAnswerService.getBySectionIdAndUserId(sectionId, userId);
        return ResponseEntity.ok(result);
    }

    /**
     * delete surveyAnswers
     *
     * @param surveyAnswerId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @DeleteMapping("/survey-answers/{surveyAnswerId}")
    @Timed
    public ResponseEntity<Void> deleteSurveySectionById(@PathVariable Long surveyAnswerId) throws WitcurveException, URISyntaxException {
        log.debug("Request to delete surveySection with id : ", surveyAnswerId);
        surveyAnswerService.deleteOne(surveyAnswerId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("A survey answer is deleted with identifier " + surveyAnswerId,
            surveyAnswerId.toString())).build();
    }
}
