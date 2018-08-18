package com.witcurve.service.mapper;

import com.witcurve.domain.SlotEventDetails;
import com.witcurve.service.dto.SlotEventDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SlotCourseDetailsMapper.class, EventMapper.class})
public interface SlotEventDetailsMapper extends EntityMapper<SlotEventDetailsDTO, SlotEventDetails> {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "scd.id", target = "scdId")
    SlotEventDetailsDTO toDto(SlotEventDetails slotEventDetails);

    @Mapping(source = "eventId", target = "event.id")
    @Mapping(source = "scdId", target = "scd.id")
    SlotEventDetails toEntity(SlotEventDetailsDTO slotEventDetailsDTO);

    default SlotEventDetails fromId(Long id) {
        if(id == null) {
            return null;
        }
        SlotEventDetails slotEventDetails = new SlotEventDetails();
        slotEventDetails.setId(id);
        return  slotEventDetails;
    }

}
