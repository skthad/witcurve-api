package com.witcurve.service.mapper;

import com.witcurve.domain.SlotEventDetails;
import com.witcurve.service.dto.SlotEventDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SlotCourseDetailsMapper.class, EventMapper.class})
public interface SlotEventDetailsMapper extends EntityMapper<SlotEventDetailsDTO, SlotEventDetails> {

    default SlotEventDetails fromId(Long id) {
        if(id == null) {
            return null;
        }
        SlotEventDetails slotEventDetails = new SlotEventDetails();
        slotEventDetails.setId(id);
        return  slotEventDetails;
    }

}
