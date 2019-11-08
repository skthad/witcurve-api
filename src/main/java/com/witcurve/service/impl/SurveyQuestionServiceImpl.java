package com.witcurve.service.impl;

import com.witcurve.domain.SurveyForm;
import com.witcurve.domain.SurveyQuestion;
import com.witcurve.domain.SurveySection;
import com.witcurve.domain.enumeration.QuestionType;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.repository.SurveyFormRepository;
import com.witcurve.repository.SurveyQuestionRepository;
import com.witcurve.repository.SurveySectionRepository;
import com.witcurve.service.SurveyQuestionService;
import com.witcurve.service.dto.SurveyQuestionDTO;
import com.witcurve.service.mapper.SurveyQuestionMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SurveyQuestionServiceImpl implements SurveyQuestionService {

    private final Logger log = LoggerFactory.getLogger(SurveyQuestionServiceImpl.class);

    @Autowired
    SurveyFormRepository surveyFormRepository;

    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    SurveySectionRepository surveySectionRepository;

    @Autowired
    SurveyQuestionMapper surveyQuestionMapper;

    @Override
    public SurveyQuestionDTO saveOrUpdate(SurveyQuestionDTO surveyQuestionDTO) {
        log.debug("Request to save or update surveyQuestion : {}", surveyQuestionDTO);
        isValid(surveyQuestionDTO);
        SurveyQuestion surveyQuestion = surveyQuestionRepository.save(surveyQuestionMapper.toEntity(surveyQuestionDTO));
        return surveyQuestionMapper.toDto(surveyQuestion);
    }

    @Override
    public List<SurveyQuestionDTO> getByFormId(Long formId) {
        log.debug("Request to get SurveyQuestion by formId : {} :" + formId);
        Optional<SurveyForm> surveyForm = surveyFormRepository.findById(formId);
        if (!surveyForm.isPresent()) {
            throw new WitcurveException("No surveyForm found with id : " + formId);
        }
        List<SurveyQuestion> result = surveyQuestionRepository.getByFormId(formId);
        return surveyQuestionMapper.toDto(result);
    }

    @Override
    public List<SurveyQuestionDTO> getBySectionId(Long sectionId) {
        log.debug("Request to get SurveyQuestion by sectionId : {} " + sectionId);
        Optional<SurveySection> surveySection = surveySectionRepository.findById(sectionId);
        if (!surveySection.isPresent()) {
            throw new WitcurveException("No surveySection found with id : " + sectionId);
        }
        List<SurveyQuestion> result = surveyQuestionRepository.getBySectionId(sectionId);
        return surveyQuestionMapper.toDto(result);
    }

    @Override
    public void deleteOne(Long questionId) {
        log.debug("Request to delete SurveyQuestion by id : {} " + questionId);
        //if form is in draft status than only we can delete question
        Optional<SurveyQuestion> question = surveyQuestionRepository.findById(questionId);
        if (!question.isPresent()) {
            throw new WitcurveException("No surveySection found with questionId : " + questionId);
        }
        if (question.get().getSection().getForm().getStatus().equals(SurveyFormStatus.DRAFT)) {
            surveyQuestionRepository.delete(question.get());
        }
    }

    private void isValid(SurveyQuestionDTO surveyQuestionDTO) {
        SurveyForm surveyForm = surveyFormRepository.findBySectionId(surveyQuestionDTO.getSectionId());
        if (surveyForm.getStatus().equals(SurveyFormStatus.DRAFT)) {
            switch (surveyQuestionDTO.getType()) {
                case RATING:
                    if (surveyQuestionDTO.getMaxRatingValue() == null || surveyQuestionDTO.getMinRatingValue() == null) {
                        throw new WitcurveException("Require min and max rating value for QuestionType :" + surveyQuestionDTO.getType());
                    }
                    if (surveyQuestionDTO.getMinRatingValue() > surveyQuestionDTO.getMaxRatingValue()) {
                        throw new WitcurveException("Min rating value can not be more than max rating value");
                    }
                    int interval = 1;
                    if (surveyQuestionDTO.getInterval() != null) {
                        interval = surveyQuestionDTO.getInterval();
                    }
                    if (!surveyQuestionDTO.getOptions().isEmpty() || surveyQuestionDTO.getOptions() != null) {
                        List<Long> keys = surveyQuestionDTO.getOptions().keySet().stream().collect(Collectors.toList());
                        for (int i = 1; i < keys.size(); i++) {
                            if (keys.get(i) != keys.get(i - 1) + interval) {
                                throw new WitcurveException("Options are not according to given interval for Rating_Type question");
                            }
                        }
                    }
                    break;
                case LONG_ANSWER:
                case SHORT_ANSWER:
                case DICHOTOMOUS:
                case MULTIPLE_CHOICE:
                case SINGLE_CHOICE:
                    if (surveyQuestionDTO.getMaxRatingValue() != null || surveyQuestionDTO.getMinRatingValue() != null || surveyQuestionDTO.getInterval() != null) {
                        throw new WitcurveException("Max,min rating and interval value should be null for QuestionType :" + surveyQuestionDTO.getType());
                    }
                    if (surveyQuestionDTO.getType().equals(QuestionType.MULTIPLE_CHOICE) || surveyQuestionDTO.getType().equals(QuestionType.SINGLE_CHOICE)) {
                        if (surveyQuestionDTO.getOptions().isEmpty() || surveyQuestionDTO.getOptions() == null) {
                            throw new WitcurveException("Options can not be null or empty for QuestionType :" + surveyQuestionDTO.getType());
                        }
                    }
                    break;
            }
        }
    }
}
