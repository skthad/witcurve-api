package com.witcurve.service.mapper;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.domain.SlotCourseDetails;
import com.witcurve.service.dto.CourseTeacherDTO;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.service.dto.SlotCourseDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SlotCourseDetailsMapperLite extends EntityMapper<SlotCourseDetailsDTO, SlotCourseDetails> {

    //@Mapping(target = "exam", source = "examId")
    SlotCourseDetails toEntity(SlotCourseDetailsDTO slotCourseDetailsDTO);

    SlotCourseDetailsDTO toDto(SlotCourseDetails slotCourseDetails);

    default SlotCourseDetails fromId(Long id) {
        if(id == null) {
            return null;
        }
        SlotCourseDetails slotCourseDetails = new SlotCourseDetails();
        slotCourseDetails.setId(id);
        return slotCourseDetails;
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

    default CourseTeacher toCourseTeacher(CourseTeacherDTO courseTeacherDTO) {
        CourseTeacher courseTeacher = new CourseTeacher();
        courseTeacher.setId(courseTeacherDTO.getId());

        return courseTeacher;
    }

    default CourseTeacherDTO toCourseTeacherDTO(CourseTeacher courseTeacher) {
        CourseTeacherDTO courseTeacherDTO = new CourseTeacherDTO();
        courseTeacherDTO.setId(courseTeacher.getId());

        return courseTeacherDTO;
    }

}
