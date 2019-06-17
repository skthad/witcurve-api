package com.witcurve.service.mapper;

import com.witcurve.domain.SurveyAnswer;
import com.witcurve.domain.User;
import com.witcurve.service.dto.SurveyAnswerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SurveyQuestionMapper.class})
public interface SurveyAnswerMapper extends EntityMapper<SurveyAnswerDTO, SurveyAnswer> {

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "user.id", target = "userId")
    SurveyAnswerDTO toDto(SurveyAnswer surveyAnswer);

    @Mapping(source = "questionId", target = "question")
    @Mapping(source = "userId", target = "user")
    SurveyAnswer toEntity(SurveyAnswerDTO surveyAnswerDTO);

    default SurveyAnswer fromId(Long id) {
        if(id == null) {
            return null;
        }
        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setId(id);
        return surveyAnswer;
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
