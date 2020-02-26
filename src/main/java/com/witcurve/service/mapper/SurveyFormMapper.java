package com.witcurve.service.mapper;

import com.witcurve.domain.Standard;
import com.witcurve.domain.SurveyForm;
import com.witcurve.domain.SurveySubmission;
import com.witcurve.service.dto.SurveyFormDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class, SurveySectionMapper.class})
public interface SurveyFormMapper extends EntityMapper<SurveyFormDTO, SurveyForm> {

    @Mapping(source = "schoolInfo.id", target = "schoolInfoId")
    @Mapping(target = "standardIds", expression = "java(getStandardIds(surveyForm))")
    @Mapping(target = "userIds", expression = "java(getUserIds(surveyForm))")
    SurveyFormDTO toDto(SurveyForm surveyForm);

    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    @Mapping(target = "standards", expression = "java(getStandards(surveyFormDTO))")
    SurveyForm toEntity(SurveyFormDTO surveyFormDTO);

    default SurveyForm fromId(Long id) {
        if (id == null) {
            return null;
        }
        SurveyForm surveyForm = new SurveyForm();
        surveyForm.setId(id);
        return surveyForm;
    }

    default List<Standard> getStandards(SurveyFormDTO surveyFormDTO) {
        List<Standard> standards = new ArrayList<>();
        if (surveyFormDTO.getStandardIds() != null) {
            for (Long standardId : surveyFormDTO.getStandardIds()) {
                Standard standard = new Standard();
                standard.setId(standardId);
                standards.add(standard);
            }
        }
        return standards;
    }

    default List<Long> getStandardIds(SurveyForm surveyForm) {
        List<Long> standardIds = new ArrayList<>();
        for (Standard standard : surveyForm.getStandards()) {
            standardIds.add(standard.getId());
        }
        return standardIds;
    }

    default List<Long> getUserIds(SurveyForm surveyForm) {
        List<Long> userIds = new ArrayList<>();
        if (surveyForm.getSurveySubmissions() != null) {
            for (SurveySubmission surveySubmission : surveyForm.getSurveySubmissions()) {
                userIds.add(surveySubmission.getUser().getId());
            }
        }
        return userIds;
    }
}
