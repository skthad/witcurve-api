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
     * @param surveyAnswerDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/survey-answers")
    @Timed
    public ResponseEntity<List<SurveyAnswerDTO>> saveSurveyAnswer(@RequestBody @Valid List<SurveyAnswerDTO> surveyAnswerDTOs, @RequestParam Long userId) throws URISyntaxException {
        log.debug("Request Save surveyAnswer : {} ", surveyAnswerDTOs);
        try {
            List<SurveyAnswerDTO> result = surveyAnswerService.saveOrUpdate(surveyAnswerDTOs, userId);
            return ResponseEntity.ok().body(result);
        } catch (
            DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else if (e.getMessage().contains("survey_question_user_id")) {
                throw new WitcurveException("A question can be answered only once by user");
            } else {
                throw new WitcurveException("DataIntegrityViolationException occurred.");
            }
        }
    }

    /**
     * updates a surveyAnswer
     *
     * @param surveyAnswerDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/survey-answers")
    @Timed
    public ResponseEntity<List<SurveyAnswerDTO>> updateSurveyAnswer(@RequestBody @Valid List<SurveyAnswerDTO> surveyAnswerDTOs, @RequestParam Long userId) throws URISyntaxException {
        log.debug("Request to update surveyAnswer : {} ", surveyAnswerDTOs);
        try {
            List<SurveyAnswerDTO> result = surveyAnswerService.saveOrUpdate(surveyAnswerDTOs, userId);
            return ResponseEntity.ok().body(result);
        } catch (
            DataIntegrityViolationException e) {
            if (e.getMessage().contains("constraint [FK")) {
                throw new WitcurveException("Foreign key for some field might be invalid");
            } else if (e.getMessage().contains("survey_question_user_id")) {
                throw new WitcurveException("A question can be answered only once by user");
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

    /**
     * get answers
     *
     * @param questionId
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @GetMapping("/survey-answers/survey-question/{questionId}")
    @Timed
    public ResponseEntity<List<String>> getAllAnswersByQuestionId(@PathVariable Long questionId) throws WitcurveException, URISyntaxException {
        log.debug("Request to get surveyAnswer with questionId : ", questionId);
        List<String> result = surveyAnswerService.getAllAnswersByQuestionId(questionId);
        return ResponseEntity.ok(result);
    }
}
