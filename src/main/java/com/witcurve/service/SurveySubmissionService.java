package com.witcurve.service;

import com.witcurve.service.dto.SurveySubmissionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SurveySubmissionService {

    SurveySubmissionDTO save(SurveySubmissionDTO surveySubmissionDTO);

    List<SurveySubmissionDTO> getByFormId(Long formId);

    List<SurveySubmissionDTO> getByUserId(Long userId);

    Page<SurveySubmissionDTO> getFormSummaryOfEachStudent(Long formId, Pageable pageable);

}
