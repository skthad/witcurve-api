package com.witcurve.service.mapper;

import com.witcurve.domain.SurveySubmission;
import com.witcurve.domain.User;
import com.witcurve.service.dto.SurveySubmissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SurveyFormMapper.class})
public interface SurveySubmissionMapper extends EntityMapper<SurveySubmissionDTO, SurveySubmission> {

    @Mapping(source = "form.id", target = "formId")
    @Mapping(source = "user.id", target = "userId")
    SurveySubmissionDTO toDto(SurveySubmission surveySubmission);

    @Mapping(source = "formId", target = "form")
    @Mapping(source = "userId", target = "user")
    SurveySubmission toEntity(SurveySubmissionDTO surveySubmissionDTO);

    default SurveySubmission fromId(Long id) {
        if(id == null) {
            return null;
        }
        SurveySubmission surveySubmission = new SurveySubmission();
        surveySubmission.setId(id);
        return surveySubmission;
    }

    default User userFromUserId(Long userId) {
        if(userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
    
}
