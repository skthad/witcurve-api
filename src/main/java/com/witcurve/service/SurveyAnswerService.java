package com.witcurve.service;

import com.witcurve.service.dto.SurveyAnswerDTO;

import java.util.List;

public interface SurveyAnswerService {

    SurveyAnswerDTO saveOrUpdate(SurveyAnswerDTO surveyAnswerDTO);

    List<SurveyAnswerDTO> getByFormIdAndUserId(Long formId, Long userId);

    List<SurveyAnswerDTO> getBySectionIdAndUserId(Long sectionId, Long userId);

    void deleteOne(Long surveyAnswerId);
}
