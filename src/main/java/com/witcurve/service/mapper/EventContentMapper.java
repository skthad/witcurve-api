package com.witcurve.service.mapper;

import com.witcurve.domain.EventContent;
import com.witcurve.service.dto.EventContentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CourseContentMapper.class})
public interface EventContentMapper extends EntityMapper<EventContentDTO, EventContent> {

    EventContentDTO toDto(EventContent courseContent);

    EventContent toEntity(EventContentDTO courseContentDTO);

}
