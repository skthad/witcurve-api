package com.witcurve.service.mapper;

import com.witcurve.domain.SlotCourseDetails;
import com.witcurve.service.dto.SlotCourseDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CourseTeacherMapper.class, GeneralSlotDetailsMapper.class})
public interface SlotCourseDetailsMapper extends EntityMapper<SlotCourseDetailsDTO, SlotCourseDetails> {

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

}
