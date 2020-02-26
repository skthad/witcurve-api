package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.QuestionType;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.domain.enumeration.SurveyUserType;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.*;
import com.witcurve.service.SurveyAnswerService;
import com.witcurve.service.dto.SurveyAnswerDTO;
import com.witcurve.service.mapper.SurveyAnswerMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SurveyAnswerServiceImpl implements SurveyAnswerService {

    private final Logger log = LoggerFactory.getLogger(SurveyAnswerServiceImpl.class);

    @Autowired
    SurveyAnswerRepository surveyAnswerRepository;

    @Autowired
    SurveyAnswerMapper surveyAnswerMapper;

    @Autowired
    SurveySubmissionRepository surveySubmissionRepository;

    @Autowired
    SurveyFormRepository surveyFormRepository;

    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public List<SurveyAnswerDTO> saveOrUpdate(List<SurveyAnswerDTO> surveyAnswerDTOs, Long userId) {
        log.debug("Request to save or update SurveyAnswer : {} ", surveyAnswerDTOs);
        isValid(surveyAnswerDTOs, userId);
        List<SurveyAnswer> surveyAnswer = surveyAnswerRepository.saveAll(surveyAnswerMapper.toEntity(surveyAnswerDTOs));
        return surveyAnswerMapper.toDto(surveyAnswer);
    }

    @Override
    public List<SurveyAnswerDTO> getByFormIdAndUserId(Long formId, Long userId) {
        log.debug("Request to get SurveyAnswer by formId and userId : {} ", formId, userId);
        List<SurveyAnswer> surveyAnswers = surveyAnswerRepository.getByFormIdAndUserId(formId, userId);
        return surveyAnswerMapper.toDto(surveyAnswers);
    }

    @Override
    public List<SurveyAnswerDTO> getBySectionIdAndUserId(Long sectionId, Long userId) {
        log.debug("Request to get SurveyAnswer by sectionId and userId : {} ", sectionId, userId);
        List<SurveyAnswer> surveyAnswers = surveyAnswerRepository.getBySectionIdAndUserId(sectionId, userId);
        return surveyAnswerMapper.toDto(surveyAnswers);
    }

    @Override
    public void deleteOne(Long surveyAnswerId) {
        log.debug("Request to delete SurveyAnswer by surveyAnswerId : {}", surveyAnswerId);
        Optional<SurveyAnswer> surveyAnswer = surveyAnswerRepository.findById(surveyAnswerId);
        if (!surveyAnswer.isPresent()) {
            throw new WitcurveException("No SurveyAnswer is present with given id : {}  " + surveyAnswerId);
        }
        SurveyForm surveyForm = surveyAnswer.get().getQuestion().getSection().getForm();
        if (surveyForm.getStatus().equals(SurveyFormStatus.PUBLISHED)) {
            SurveySubmission surveySubmission = surveySubmissionRepository.findByFormIdAndUserId(surveyForm.getId(), surveyAnswer.get().getUser().getId());
            if (surveySubmission != null) {
                throw new WitcurveException("Can't delete answer of already submitted SurveyForm");
            }
            surveyAnswerRepository.delete(surveyAnswer.get());
        }
    }

    @Override
    public List<String> getAllAnswersByQuestionId(Long questionId) {
        log.debug("Request to get answers by questionId : {}", questionId);
        Optional<SurveyQuestion> surveyQuestion = surveyQuestionRepository.findById(questionId);
        if (!surveyQuestion.isPresent()) {
            throw new WitcurveException("No question is present with given id : {}" + questionId);
        }
        if (!surveyQuestion.get().getType().equals(QuestionType.LONG_ANSWER) && !surveyQuestion.get().getType().equals(QuestionType.SHORT_ANSWER)) {
            throw new WitcurveException("We can get all answers for long answer type and short answer type questions only");
        }
        return surveyAnswerRepository.getAllAnswersByQuestionId(questionId);
    }

    private void isValid(List<SurveyAnswerDTO> surveyAnswerDTOs, Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new WitcurveException("No user present with given id : {}" + userId);
        }
        String answer;
        Collection<String> options;
        for (SurveyAnswerDTO surveyAnswerDTO : surveyAnswerDTOs) {
            Optional<SurveyQuestion> surveyQuestion = surveyQuestionRepository.findById(surveyAnswerDTO.getQuestionId());
            if (!surveyQuestion.isPresent()) {
                throw new WitcurveException("No SurveyQuestion is present with given id : {}" + surveyAnswerDTO.getQuestionId());
            }
            if (!surveyQuestion.get().getSection().getForm().getStatus().equals(SurveyFormStatus.PUBLISHED)) {
                throw new WitcurveException("Answers can be saved only when form is published");
            }
            if (surveyQuestion.get().getSection().getForm().getType().equals(SurveyUserType.PARENT) && !user.get().getType().equals(UserType.PARENT)) {
                throw new WitcurveException("This question can not be answered by you");
            }
            if (surveyQuestion.get().getSection().getForm().getType().equals(SurveyUserType.STAFF) && !user.get().getType().equals(UserType.TEACHING_STAFF)) {
                throw new WitcurveException("This question can not be answered by you");
            }
            surveyAnswerDTO.setUserId(userId);
            switch (surveyQuestion.get().getType()) {
                case SHORT_ANSWER:
                case LONG_ANSWER:
                    if (surveyAnswerDTO.getAnswers().size() > 1 || surveyAnswerDTO.getAnswers().size() == 0) {
                        throw new WitcurveException("Answer is either empty or having more than one value for Short answer type question");
                    }
                    if (surveyQuestion.get().getType().equals(QuestionType.SHORT_ANSWER)) {
                        if (surveyAnswerDTO.getAnswers().get(0).length() > 80) {
                            throw new WitcurveException("Size of answer can not be more than 80 for Short answer type question");
                        }
                    }
                    if (surveyQuestion.get().getType().equals(QuestionType.LONG_ANSWER)) {
                        if (surveyAnswerDTO.getAnswers().get(0).length() > 500) {
                            throw new WitcurveException("Size of answer can not be more than one 500 for Long answer type question");
                        }
                    }
                    break;
                case DICHOTOMOUS:
                    if (surveyAnswerDTO.getAnswers().size() > 1 || surveyAnswerDTO.getAnswers().size() == 0) {
                        throw new WitcurveException("Answer can not be more than one for Dichotomous type question");
                    }
                    answer = surveyAnswerDTO.getAnswers().get(0).toUpperCase();
                    if (!("TRUE".equals(answer) || "FALSE".equals(answer))) {
                        throw new WitcurveException("Answer should be in true or false for Dichotomous type question");
                    }
                    surveyAnswerDTO.setAnswers(Arrays.asList(answer));
                    break;
                case SINGLE_CHOICE:
                    if (surveyAnswerDTO.getAnswers().size() > 1 || surveyAnswerDTO.getAnswers().size() == 0) {
                        throw new WitcurveException("Answer can not be more than one for Single choice type question");
                    }
                    options = surveyQuestion.get().getOptions().values();
                    if (surveyQuestion.get().getOtherField().equals(false)) {
                        if (options.stream().noneMatch(s -> s.equalsIgnoreCase(surveyAnswerDTO.getAnswers().get(0)))) {
                            throw new WitcurveException("Selected answer is not in question's option list");
                        }
                    }
                    break;
                case MULTIPLE_CHOICE:
                    if (surveyAnswerDTO.getAnswers().size() == 0) {
                        throw new WitcurveException("Answer can not be empty or null");
                    }
                    List<String> selectedAnswers = surveyAnswerDTO.getAnswers();
                    options = surveyQuestion.get().getOptions().values();
                    if (surveyQuestion.get().getOtherField().equals(false)) {
                        if (selectedAnswers.size() > options.size()) {
                            throw new WitcurveException("Selected ans can not be more than given options");
                        }
                        for (String selectedAnswer : selectedAnswers) {
                            if (options.stream().noneMatch(s -> s.equalsIgnoreCase(selectedAnswer))) {
                                throw new WitcurveException("Selected answer is not in question's option list");
                            }
                        }
                    } else {//when otherField value is true
                        if (selectedAnswers.size() > options.size() + 1) {
                            throw new WitcurveException("Multiple choice type question can not have two other values");
                        }
                        int count = 0;
                        for (String selectedAnswer : selectedAnswers) {
                            if (options.stream().noneMatch(s -> s.equalsIgnoreCase(selectedAnswer))) {
                                count++;
                            }
                        }
                        if (count > 1) {
                            throw new WitcurveException("Selected answers do not match the given options");
                        }
                    }
                    break;
                case RATING:
                    answer = surveyAnswerDTO.getAnswers().get(0);
                    List<Integer> expectedKeys = new ArrayList<>();

                    int value = surveyQuestion.get().getMinRatingValue();
                    expectedKeys.add(value);

                    int interval = 1;
                    if (surveyQuestion.get().getInterval() != null) {
                        interval = surveyQuestion.get().getInterval();
                    }
                    while (value + interval <= surveyQuestion.get().getMaxRatingValue()) {
                        value = value + interval;
                        expectedKeys.add(value);
                    }
                    if (!expectedKeys.contains(Integer.parseInt(answer))) {
                        throw new WitcurveException("Selected answer does not match the given options");
                    }
                    break;
            }
        }
    }
}


