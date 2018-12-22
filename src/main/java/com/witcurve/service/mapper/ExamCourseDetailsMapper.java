package com.witcurve.service.mapper;

import com.witcurve.domain.ExamCourseDetails;
import com.witcurve.service.dto.ExamCourseDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CourseTeacherMapper.class, GeneralSlotDetailsMapper.class})
public interface ExamCourseDetailsMapper extends EntityMapper<ExamCourseDetailsDTO, ExamCourseDetails> {


    ExamCourseDetails toEntity(ExamCourseDetailsDTO examCourseDetailsDTO);


    ExamCourseDetailsDTO toDto(ExamCourseDetails examCourseDetails);

    default ExamCourseDetails fromId(Long id) {
        if(id == null) {
            return null;
        }
        ExamCourseDetails examCourseDetails = new ExamCourseDetails();
        examCourseDetails.setId(id);
        return examCourseDetails;
    }

}
