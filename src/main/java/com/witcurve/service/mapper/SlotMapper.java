package com.witcurve.service.mapper;

import com.witcurve.domain.Slot;
import com.witcurve.service.dto.SlotDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SlotMapper {

    @Mapping(source = "standard.id", target = "standardId")
    SlotDTO slotToSlotDTO(Slot slot);

    @Mapping(target = "standard.id", source = "standardId")
    Slot slotDTOToSlot(SlotDTO slotDTO);

    List<Slot> slotDTOsToSlots(List<SlotDTO> slotDTOS);

    List<SlotDTO> slotsToSlotDTOs(List<Slot> slots);
}
