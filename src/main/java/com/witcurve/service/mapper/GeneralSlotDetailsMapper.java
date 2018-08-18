package com.witcurve.service.mapper;

import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClassMapper.class})
public interface GeneralSlotDetailsMapper extends EntityMapper<GeneralSlotDetailsDTO, GeneralSlotDetails> {


    @Mapping(source = "standard.id", target = "standardId")
    GeneralSlotDetailsDTO toDto(GeneralSlotDetails generalSlotDetails);

    @Mapping(target = "standard", source = "standardId")
    GeneralSlotDetails toEntity(GeneralSlotDetailsDTO generalSlotDetailsDTO);

    default GeneralSlotDetails fromId(Long id) {
        if(id == null) {
            return  null;
        }
        GeneralSlotDetails generalSlotDetails = new GeneralSlotDetails();
        generalSlotDetails.setId(id);
        return generalSlotDetails;
    }
}
