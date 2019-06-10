package com.witcurve.service.mapper;

import com.witcurve.domain.EventContent;
import com.witcurve.domain.ExamCourseDetails;
import com.witcurve.service.dto.ExamCourseDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {CourseMapper.class, GeneralSlotDetailsMapper.class})
public interface ExamCourseDetailsMapper extends EntityMapper<ExamCourseDetailsDTO, ExamCourseDetails> {

    ExamCourseDetails toEntity(ExamCourseDetailsDTO examCourseDetailsDTO);

    @Mapping(target = "courseContentAttached", expression = "java(courseContentAttached(examCourseDetails.getEventContents()))")
    ExamCourseDetailsDTO toDto(ExamCourseDetails examCourseDetails);

    default ExamCourseDetails fromId(Long id) {
        if(id == null) {
            return null;
        }
        ExamCourseDetails examCourseDetails = new ExamCourseDetails();
        examCourseDetails.setId(id);
        return examCourseDetails;
    }

    default Boolean courseContentAttached(Set<EventContent> eventContents) {
        if(eventContents != null && eventContents.size()!=0) {
            return true;
        } else {
            return false;
        }
    }

}
