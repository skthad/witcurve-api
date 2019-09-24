package com.witcurve.service.mapper;

import com.witcurve.domain.AttributeValue;
import com.witcurve.service.dto.AttributeValueDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {StudentMapper.class,AttributeMapper.class})
public interface AttributeValueMapper extends EntityMapper<AttributeValueDTO, AttributeValue> {

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "attributeId", source = "attribute.id")
    AttributeValueDTO toDto(AttributeValue attributeValue);


    @Mapping(source = "attributeId", target = "attribute")
    @Mapping(source = "studentId", target = "student")
    AttributeValue toEntity(AttributeValueDTO attributeValueDTO);


    default AttributeValue fromId(Long id) {
        if(id==null) {
            return null;
        }
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(id);
        return attributeValue;
    }



}
