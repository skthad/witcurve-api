package com.witcurve.service.mapper;

import com.witcurve.domain.Slot;
import com.witcurve.service.dto.SlotDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClassMapper.class})
public interface SlotMapper extends EntityMapper<SlotDTO, Slot>{

    @Mapping(source = "standard.id", target = "standardId")
    SlotDTO toDto(Slot slot);

    @Mapping(target = "standard.id", source = "standardId")
    Slot toEntity(SlotDTO slotDTO);

    default Slot fromId(Long id) {
        if(id == null) {
            return null;
        }
        Slot slot = new Slot();
        slot.setId(id);
        return slot;
    }
}
