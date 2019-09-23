package com.witcurve.service.mapper;

import com.witcurve.domain.Attribute;
import com.witcurve.service.dto.AttributeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring", uses = ReportCardDesignMapper.class)
public interface AttributeMapper extends EntityMapper<AttributeDTO, Attribute> {

    @Mapping(source = "reportCardDesign.id", target = "rcdId")
    AttributeDTO toDto(Attribute attribute);

    @Mapping(source = "rcdId", target = "reportCardDesign")
    Attribute toEntity(AttributeDTO attributeDTO);


    List<AttributeDTO> toListDTO(Set<Attribute> attributeSet);



    default Attribute fromId(Long id) {
        if(id == null) {
            return null;
        }
        Attribute attribute = new Attribute();
        attribute.setId(id);
        return attribute;
    }

}
