package com.witcurve.service.mapper;

import com.witcurve.domain.StandardReport;
import com.witcurve.service.impl.StandardReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StandardMapper.class, ReportCardMapper.class})
public interface StandardReportMapper {

    @Mapping(source = "standard.id", target = "standardId")
    @Mapping(source = "reportCard.id", target = "reportCardId")
    StandardReportDTO toDto(StandardReport standardReport);

    @Mapping(target = "standard", source = "standardId")
    @Mapping(target = "reportCard", source = "reportCardId")
    StandardReport toEntity(StandardReportDTO standardReportDTO);

    default StandardReport fromId(Long id) {
        if (id == null) {
            return null;
        }
        StandardReport standardReport = new StandardReport();
        standardReport.setId(id);
        return standardReport;
    }

}
