package com.witcurve.service.mapper;

import com.witcurve.domain.SurveySection;
import com.witcurve.service.dto.SurveySectionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SurveyFormMapper.class})
public interface SurveySectionMapper extends EntityMapper<SurveySectionDTO, SurveySection>{

    @Mapping(source = "form.id", target = "formId")
    SurveySectionDTO toDto(SurveySection surveySection);

    @Mapping(source = "formId", target = "form")
    SurveySection toEntity(SurveySectionDTO surveySectionDTO);

    default SurveySection fromId(Long id) {
        if(id == null) {
            return null;
        }
        SurveySection surveySection = new SurveySection();
        surveySection.setId(id);
        return surveySection;
    }
}
