package com.witcurve.service.mapper;

import com.witcurve.domain.ReportCard;
import com.witcurve.domain.ScholasticReportDetails;
import com.witcurve.service.dto.ScholasticReportDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ReportCardMapper.class, ReportCardDesignMapper.class})
public interface ScholasticReportDetailsMapper extends EntityMapper<ScholasticReportDetailsDTO, ScholasticReportDetails> {

    @Mapping(source = "reportCardDesign.id", target = "reportCardDesignId")
    @Mapping(source = "reportCard.id", target = "reportCardId")
    ScholasticReportDetailsDTO toDto(ScholasticReportDetails scholasticReportDetails);

    @Mapping(source = "reportCardDesignId", target = "reportCardDesign")
    @Mapping(source = "reportCardId", target = "reportCard")
    ScholasticReportDetails toEntity(ScholasticReportDetailsDTO scholasticReportDetailsDTO);

    default ScholasticReportDetails fromId(Long id) {
        if(id == null) {
            return null;
        }
        ScholasticReportDetails scholasticReportDetails = new ScholasticReportDetails();
        scholasticReportDetails.setId(id);
        return scholasticReportDetails;
    }


}
