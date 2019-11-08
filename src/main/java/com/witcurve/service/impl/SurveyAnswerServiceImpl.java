package com.witcurve.service.impl;

import com.witcurve.domain.SurveyAnswer;
import com.witcurve.domain.SurveyForm;
import com.witcurve.domain.SurveyQuestion;
import com.witcurve.domain.SurveySubmission;
import com.witcurve.domain.enumeration.QuestionType;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.repository.SurveyAnswerRepository;
import com.witcurve.repository.SurveyFormRepository;
import com.witcurve.repository.SurveyQuestionRepository;
import com.witcurve.repository.SurveySubmissionRepository;
import com.witcurve.service.SurveyAnswerService;
import com.witcurve.service.dto.SurveyAnswerDTO;
import com.witcurve.service.mapper.SurveyAnswerMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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


    @Override
    public SurveyAnswerDTO saveOrUpdate(SurveyAnswerDTO surveyAnswerDTO) {
        log.debug("Request to save or update SurveyAnswer : {} ", surveyAnswerDTO);
        isValid(surveyAnswerDTO);
        SurveyAnswer surveyAnswer = surveyAnswerRepository.save(surveyAnswerMapper.toEntity(surveyAnswerDTO));
        return surveyAnswerMapper.toDto(surveyAnswer);
    }

    @Override
    public List<SurveyAnswerDTO> getByFormIdAndUserId(Long formId, Long userId) {
        //check it by giving userId and formId which does not exists
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
            throw new WitcurveException("No SurveyAnswer is present with given id : {}" + surveyAnswerId);
        }
        SurveyForm surveyForm = surveyAnswerRepository.getSurveyFormByAnsId(surveyAnswerId);
        if (surveyForm.getStatus().equals(SurveyFormStatus.PUBLISHED)) {
            SurveySubmission surveySubmission = surveySubmissionRepository.findByFormAndUserId(surveyForm.getId(), surveyAnswer.get().getUser().getId());
            if (surveySubmission != null) {
                throw new WitcurveException("Can't delete answer of already submitted SurveyForm");
            }
            surveyAnswerRepository.delete(surveyAnswer.get());
        }
    }
    private void isValid(SurveyAnswerDTO surveyAnswerDTO) {
        String answer;
        Collection<String> options;
        ///SurveyForm surveyForm = surveyFormRepository.findByQuestionId(surveyAnswerDTO.getQuestionId());
        Optional<SurveyQuestion> surveyQuestion = surveyQuestionRepository.findById(surveyAnswerDTO.getQuestionId());
        if (surveyQuestion.get().getSection().getForm().getStatus().equals(SurveyFormStatus.PUBLISHED)) {
            // Optional<SurveyQuestion> surveyQuestion = surveyQuestionRepository.findById(surveyAnswerDTO.getQuestionId());
            switch (surveyQuestion.get().getType()) {
                case SHORT_ANSWER:
                case LONG_ANSWER:
                    if (surveyAnswerDTO.getAnswers().size() > 1 || surveyAnswerDTO.getAnswers().size() == 0) {
                        throw new WitcurveException("Answer is either empty or having more than one value for Short_Answer_Type Question");
                    }
                    if (surveyQuestion.get().getType().equals(QuestionType.SHORT_ANSWER)) {
                        if (surveyAnswerDTO.getAnswers().get(0).length() > 80) {
                            throw new WitcurveException("Size of answer can not be more than 80 for Short_Answer_Type Question");
                        }
                    }
                    if (surveyQuestion.get().getType().equals(QuestionType.LONG_ANSWER)) {
                        if (surveyAnswerDTO.getAnswers().get(0).length() > 500) {
                            throw new WitcurveException("Size of answer can not be more than one 500 for Long_Answer_Type Question");
                        }
                    }
                    break;
                case DICHOTOMOUS:
                    if (surveyAnswerDTO.getAnswers().size() > 1 || surveyAnswerDTO.getAnswers().size() == 0) {
                        throw new WitcurveException("Size of list can not be more than one for Dichotomous_Type Question");
                    }
                    answer= surveyAnswerDTO.getAnswers().get(0).toUpperCase();
                    if (!answer.equals(Boolean.TRUE) && !answer.equals(Boolean.FALSE)) {
                        throw new WitcurveException("Dichotomous_Type question can have value either true or false");
                    }
                    break;
                case SINGLE_CHOICE:
                    if (surveyAnswerDTO.getAnswers().size() > 1 || surveyAnswerDTO.getAnswers().size() == 0) {
                        throw new WitcurveException("Size of list can not be more than one for Single_Choice_Type Question");
                    }
                    answer = surveyAnswerDTO.getAnswers().get(0);
                    options = surveyQuestion.get().getOptions().values();
                    if (surveyQuestion.get().getOtherField().equals(false)) {
                        if (options.stream().anyMatch(answer::equalsIgnoreCase)) {
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
                        for (String ans : selectedAnswers) {
                            if (!options.stream().anyMatch(ans::equalsIgnoreCase)) {
                                throw new WitcurveException("Selected answer is not in question's option list");
                            }
                        }
                    } else {//when otherField value is true
                        if (selectedAnswers.size() > options.size() + 1) {
                            throw new WitcurveException("Multiple_choice_Type question can not have two other values");
                        }
                        int count = 0;
                        for (String ans : selectedAnswers) {
                            if (!options.stream().anyMatch(ans::equalsIgnoreCase)) {
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
                    options = surveyQuestion.get().getOptions().values();
                    if (surveyQuestion.get().getOtherField().equals(false)) {
                        if (options.stream().anyMatch(answer::equalsIgnoreCase)) {
                            throw new WitcurveException("Selected answer is not in question's option list");
                        }
                    }
            }
        }
    }
}
