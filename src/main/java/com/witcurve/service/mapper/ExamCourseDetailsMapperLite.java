package com.witcurve.service.mapper;

import com.witcurve.domain.ExamCourseDetails;
import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.service.dto.ExamCourseDetailsDTO;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface ExamCourseDetailsMapperLite extends EntityMapper<ExamCourseDetailsDTO, ExamCourseDetails> {


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

    default GeneralSlotDetails toGeneralSlotDetails(GeneralSlotDetailsDTO generalSlotDetailsDTO) {
        GeneralSlotDetails generalSlotDetails = new GeneralSlotDetails();
        generalSlotDetails.setId(generalSlotDetailsDTO.getId());

        return generalSlotDetails;
    }

    default GeneralSlotDetailsDTO toGeneralSlotDetailsDTO(GeneralSlotDetails generalSlotDetails) {
        GeneralSlotDetailsDTO generalSlotDetailsDTO = new GeneralSlotDetailsDTO();
        generalSlotDetailsDTO.setId(generalSlotDetails.getId());

        return generalSlotDetailsDTO;
    }

}
