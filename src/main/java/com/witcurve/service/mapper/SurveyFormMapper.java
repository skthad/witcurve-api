package com.witcurve.service.mapper;

import com.witcurve.domain.SurveyForm;
import com.witcurve.service.dto.SurveyFormDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class, SurveySectionMapper.class})
public interface SurveyFormMapper extends EntityMapper<SurveyFormDTO, SurveyForm> {

    @Mapping(source = "schoolInfo.id", target = "schoolInfoId")
    SurveyFormDTO toDto(SurveyForm surveyForm);

    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    SurveyForm toEntity(SurveyFormDTO surveyFormDTO);

    default SurveyForm fromId(Long id) {
        if(id == null) {
            return null;
        }
        SurveyForm surveyForm = new SurveyForm();
        surveyForm.setId(id);
        return surveyForm;
    }

}
