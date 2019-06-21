package com.witcurve.service.mapper;

import com.witcurve.domain.SurveyQuestion;
import com.witcurve.service.dto.SurveyQuestionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SurveySectionMapper.class})
public interface SurveyQuestionMapper extends EntityMapper<SurveyQuestionDTO, SurveyQuestion>{

    @Mapping(source = "section.id", target = "sectionId")
    SurveyQuestionDTO toDto(SurveyQuestion surveyQuestion);

    @Mapping(source = "sectionId", target = "section")
    SurveyQuestion toEntity(SurveyQuestionDTO surveyQuestionDTO);

    default SurveyQuestion fromId(Long id) {
        if(id == null) {
            return null;
        }
        SurveyQuestion surveyQuestion = new SurveyQuestion();
        surveyQuestion.setId(id);
        return surveyQuestion;
    }
    
}
