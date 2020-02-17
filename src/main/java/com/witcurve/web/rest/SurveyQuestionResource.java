package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.SurveyQuestionService;
import com.witcurve.service.dto.SurveyQuestionDTO;
import com.witcurve.web.rest.errors.WitcurveException;
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
public class SurveyQuestionResource {

    private final Logger log = LoggerFactory.getLogger(SurveyQuestionResource.class);

    @Autowired
    SurveyQuestionService surveyQuestionService;

    /**
     * creates a surveyQuestion
     *
     * @param surveyQuestionDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/survey-questions")
    @Timed
    public ResponseEntity<List<SurveyQuestionDTO>> saveSurveyQuestion(@RequestBody @Valid List<SurveyQuestionDTO> surveyQuestionDTOs, @RequestParam Long sectionId) throws URISyntaxException {
        log.debug("Request Save surveyQuestion : {} ", surveyQuestionDTOs);
        List<SurveyQuestionDTO> result = surveyQuestionService.saveOrUpdate(surveyQuestionDTOs, sectionId);
        return ResponseEntity.ok().body(result);
    }

    /**
     * updates the given surveyQuestion
     *
     * @param surveyQuestionDTOs
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PutMapping("/survey-questions")
    @Timed
    public ResponseEntity<List<SurveyQuestionDTO>> updateSurveyQuestion(@RequestBody @Valid List<SurveyQuestionDTO>
                                                                            surveyQuestionDTOs, @RequestParam Long sectionId) throws URISyntaxException {
        log.debug("Request to update surveyQuestion : {} ", surveyQuestionDTOs);
        List<SurveyQuestionDTO> result = surveyQuestionService.saveOrUpdate(surveyQuestionDTOs, sectionId);
        return ResponseEntity.ok().body(result);
    }

    /**
     * get surveyQuestion by surveyFormId
     *
     * @param surveyFormId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/survey-questions/survey-forms/{surveyFormId}")
    @Timed
    public ResponseEntity<List<SurveyQuestionDTO>> getSurveyQuestionByFormId(@PathVariable("surveyFormId") Long surveyFormId) {
        log.debug("Request to get surveyQuestion by formId : {} ", surveyFormId);
        List<SurveyQuestionDTO> result = surveyQuestionService.getByFormId(surveyFormId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get surveyQuestion by sectionId
     *
     * @param sectionId
     * @return
     * @throws WitcurveException
     */
    @GetMapping("/survey-questions/survey-sections/{sectionId}")
    @Timed
    public ResponseEntity<List<SurveyQuestionDTO>> getSurveyQuestionBySectionId(@PathVariable("sectionId") Long sectionId) {
        log.debug("Request to get surveyQuestion by sectionId : {} ", sectionId);
        List<SurveyQuestionDTO> result = surveyQuestionService.getBySectionId(sectionId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete surveyQuestion by id
     *
     * @param surveyQuestionId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/survey-questions/{surveyQuestionId}")
    @Timed
    public ResponseEntity<Void> deleteSurveyFromById(@PathVariable Long surveyQuestionId) throws WitcurveException, URISyntaxException {
        log.debug("Request to delete surveyQuestion by id : {} ", surveyQuestionId);
        surveyQuestionService.deleteOne(surveyQuestionId);
        return ResponseEntity.ok(null);
    }
}
