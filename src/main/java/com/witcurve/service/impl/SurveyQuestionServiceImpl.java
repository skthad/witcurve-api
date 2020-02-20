package com.witcurve.service.impl;

import com.witcurve.domain.SurveyQuestion;
import com.witcurve.domain.SurveySection;
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

import java.util.ArrayList;
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
    public List<SurveyQuestionDTO> saveOrUpdate(List<SurveyQuestionDTO> surveyQuestionDTOs, Long sectionId) {
        log.debug("Request to save or update surveyQuestion : {}", surveyQuestionDTOs);
        isValid(surveyQuestionDTOs, sectionId);
        List<SurveyQuestion> surveyQuestions = surveyQuestionMapper.toEntity(surveyQuestionDTOs);
        surveyQuestions = surveyQuestionRepository.saveAll(surveyQuestions);
        return surveyQuestionMapper.toDto(surveyQuestions);
    }

    @Override
    public List<SurveyQuestionDTO> getByFormId(Long formId) {
        log.debug("Request to get SurveyQuestion by formId : {} :" + formId);
        List<SurveyQuestion> result = surveyQuestionRepository.getByFormId(formId);
        return surveyQuestionMapper.toDto(result);
    }

    @Override
    public List<SurveyQuestionDTO> getBySectionId(Long sectionId) {
        log.debug("Request to get SurveyQuestion by sectionId : {} " + sectionId);
        List<SurveyQuestion> result = surveyQuestionRepository.getBySectionId(sectionId);
        return surveyQuestionMapper.toDto(result);
    }

    @Override
    public void deleteOne(Long questionId) {
        log.debug("Request to delete SurveyQuestion by id : {} " + questionId);
        Optional<SurveyQuestion> question = surveyQuestionRepository.findById(questionId);
        if (!question.isPresent()) {
            throw new WitcurveException("No surveyQuestion found with id : " + questionId);
        }
        if (question.get().getSection().getForm().getStatus().equals(SurveyFormStatus.DRAFT)) {
            surveyQuestionRepository.delete(question.get());
        } else {
            throw new WitcurveException("Question can not be deleted if form is in Published or Closed status");
        }
    }

    private void isValid(List<SurveyQuestionDTO> surveyQuestionDTOs, Long sectionId) {
        Optional<SurveySection> surveySection = surveySectionRepository.findById(sectionId);
        if (!surveySection.isPresent()) {
            throw new WitcurveException("No SurveySection is present with given id");
        }
        if (!surveySection.get().getForm().getStatus().equals(SurveyFormStatus.DRAFT)) {
            throw new WitcurveException("Questions can not be saved when form is in Published or closed state");
        }
        for (SurveyQuestionDTO surveyQuestionDTO : surveyQuestionDTOs) {
            surveyQuestionDTO.setSectionId(sectionId);
            switch (surveyQuestionDTO.getType()) {
                case RATING:
                    if (surveyQuestionDTO.getMaxRatingValue() == null || surveyQuestionDTO.getMinRatingValue() == null) {
                        throw new WitcurveException("Require min and max rating value for Rating question type");
                    }
                    if (surveyQuestionDTO.getMinRatingValue() > surveyQuestionDTO.getMaxRatingValue()) {
                        throw new WitcurveException("Min rating value can not be more than max rating value");
                    }
                    int interval = 1;
                    if (surveyQuestionDTO.getInterval() != null) {
                        interval = surveyQuestionDTO.getInterval();
                    }
                    if (surveyQuestionDTO.getOptions() != null) {
                        List<Integer> expectedKeys = new ArrayList<>();
                        int value = surveyQuestionDTO.getMinRatingValue();
                        expectedKeys.add(value);
                        while (value + interval <= surveyQuestionDTO.getMaxRatingValue()) {
                            value = value + interval;
                            expectedKeys.add(value);
                        }
                        List<Integer> keys = surveyQuestionDTO.getOptions().keySet().stream().collect(Collectors.toList());
                        for (Integer key : keys) {
                            if (!expectedKeys.contains(key)) {
                                throw new WitcurveException("Given options are not according to interval");
                            }
                        }
                    }
                    break;
                case MULTIPLE_CHOICE:
                case SINGLE_CHOICE:
                    if (surveyQuestionDTO.getOptions().isEmpty() || surveyQuestionDTO.getOptions() == null) {
                        throw new WitcurveException("Options can not be null or empty");
                    }
                    break;
            }
        }
    }
}

