package com.witcurve.service.mapper;

import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.domain.SlotCourseDetails;
import com.witcurve.service.dto.SlotCourseDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CourseMapper.class, GeneralSlotDetailsMapper.class})
public interface SlotCourseDetailsMapper extends EntityMapper<SlotCourseDetailsDTO, SlotCourseDetails> {

    @Mapping(source = "courseId", target = "course")
    @Mapping(source = "gsdId", target = "gsd")
    SlotCourseDetails toEntity(SlotCourseDetailsDTO slotCourseDetailsDTO);

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "gsd.id", target = "gsdId")
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
