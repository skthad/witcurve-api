package com.witcurve.service.impl;

import com.witcurve.domain.SurveyAnswer;
import com.witcurve.domain.SurveyForm;
import com.witcurve.domain.SurveyQuestion;
import com.witcurve.domain.SurveySubmission;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.repository.*;
import com.witcurve.service.SurveySubmissionService;
import com.witcurve.service.dto.SurveyAnswerDTO;
import com.witcurve.service.dto.SurveySubmissionDTO;
import com.witcurve.service.mapper.SurveyAnswerMapper;
import com.witcurve.service.mapper.SurveySubmissionMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SurveySubmissionServiceImpl implements SurveySubmissionService {

    private final Logger log = LoggerFactory.getLogger(SurveySubmissionServiceImpl.class);

    @Autowired
    SurveySubmissionRepository surveySubmissionRepository;

    @Autowired
    SurveySubmissionMapper surveySubmissionMapper;

    @Autowired
    SurveyFormRepository surveyFormRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    SurveyAnswerRepository surveyAnswerRepository;

    @Autowired
    SurveyAnswerMapper surveyAnswerMapper;

    @Override
    public SurveySubmissionDTO save(SurveySubmissionDTO surveySubmissionDTO) {
        log.debug("Request to save or update SurveySubmission {} :", surveySubmissionDTO);
        Optional<SurveyForm> surveyForm = surveyFormRepository.findById(surveySubmissionDTO.getFormId());
        if (!surveyForm.isPresent()) {
            throw new WitcurveException("Survey form is not present with id :{} " + surveySubmissionDTO.getFormId());
        }
        if (!surveyForm.get().getStatus().equals(SurveyFormStatus.PUBLISHED)) {
            throw new WitcurveException("Survey form can not be submitted when form is in draft or closed state");
        }
        List<SurveyQuestion> mandatoryUnansweredQuestions = surveyQuestionRepository.getMandatoryUnansweredRecordByUserIdAndFormId(surveySubmissionDTO.getUserId(), surveySubmissionDTO.getFormId());
        if (mandatoryUnansweredQuestions.size() > 0) {
            throw new WitcurveException("All mandatory questions should be answered before submitting form");
        }
        SurveySubmission surveySubmission = surveySubmissionRepository.save(surveySubmissionMapper.toEntity(surveySubmissionDTO));
        return surveySubmissionMapper.toDto(surveySubmission);
    }

    @Override
    public List<SurveySubmissionDTO> getByFormId(Long formId) {
        log.debug("Request to get SurveySubmission by formId {} :", formId);
        List<SurveySubmission> surveySubmissions = surveySubmissionRepository.findByFormId(formId);
        return surveySubmissionMapper.toDto(surveySubmissions);
    }

    @Override
    public List<SurveySubmissionDTO> getByUserId(Long userId) {
        log.debug("Request to get SurveySubmission by userId {} : ", userId);
        List<SurveySubmission> surveySubmissions = surveySubmissionRepository.getByUserId(userId);
        return surveySubmissionMapper.toDto(surveySubmissions);
    }

    @Override
    public Page<SurveySubmissionDTO> getFormSummaryOfEachStudent(Long formId, Pageable pageable) {
        log.debug("Request to get SurveySubmission by formId {} : ", formId);
        List<SurveySubmissionDTO> surveySubmissionDTOS = new ArrayList<>();
        Page<SurveySubmission> surveySubmissions = surveySubmissionRepository.findByFormIdUsingPageable(formId, pageable);
        for (SurveySubmission surveySubmission : surveySubmissions) {
            SurveySubmissionDTO surveySubmissionDTO = surveySubmissionMapper.toDto(surveySubmission);

            List<SurveyAnswer> answersOfParticularUser = surveyAnswerRepository.getByFormIdAndUserId(formId, surveySubmission.getUser().getId());
            List<SurveyAnswerDTO> surveyAnswerDTOS = surveyAnswerMapper.toDto(answersOfParticularUser);

            surveySubmissionDTO.setUserName(surveySubmission.getUser().getFirstName() + " " + surveySubmission.getUser().getLastName());
            surveySubmissionDTO.setAnswers(surveyAnswerDTOS);
            surveySubmissionDTOS.add(surveySubmissionDTO);
        }
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > surveySubmissionDTOS.size() ? surveySubmissionDTOS.size() : (start + pageable.getPageSize());
        return new PageImpl<SurveySubmissionDTO>(surveySubmissionDTOS.subList(start, end), pageable, surveySubmissionDTOS.size());
    }
}
