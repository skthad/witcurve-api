package com.witcurve.service;

import com.witcurve.service.dto.SurveyQuestionDTO;

import java.util.List;

public interface SurveyQuestionService {

    List<SurveyQuestionDTO> saveOrUpdate(List<SurveyQuestionDTO> surveyQuestionDTO, Long sectionId);

    List<SurveyQuestionDTO> getByFormId(Long surveyFormId);

    List<SurveyQuestionDTO> getBySectionId(Long sectionId);

    void deleteOne(Long id);
}
