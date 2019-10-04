package com.witcurve.service.mapper;

import com.witcurve.domain.NonScholasticReportDetails;
import com.witcurve.service.dto.NonScholasticReportDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ReportCardMapper.class, ReportCardDesignMapper.class})
public interface NonScholasticReportDetailsMapper extends EntityMapper<NonScholasticReportDetailsDTO, NonScholasticReportDetails>{

    @Mapping(source = "reportCardDesign.id", target = "reportCardDesignId")
    @Mapping(source = "reportCard.id", target = "reportCardId")
    NonScholasticReportDetailsDTO toDto(NonScholasticReportDetails nonScholasticReportDetails);

    @Mapping(source = "reportCardDesignId", target = "reportCardDesign")
    @Mapping(source = "reportCardId", target = "reportCard")
   NonScholasticReportDetails toEntity(NonScholasticReportDetailsDTO nonScholasticReportDetailsDTO);

    default NonScholasticReportDetails fromId(Long id) {
        if(id == null) {
            return null;
        }
        NonScholasticReportDetails nonScholasticReportDetails = new NonScholasticReportDetails();
        nonScholasticReportDetails.setId(id);
        return nonScholasticReportDetails;
    }
    
}
