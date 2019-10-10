package com.witcurve.service.mapper;

import com.witcurve.domain.Standard;
import com.witcurve.domain.StandardReport;
import com.witcurve.service.impl.StandardReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StandardMapper.class, ReportCardMapper.class})
public interface StandardReportMapper  extends EntityMapper<StandardReportDTO, StandardReport>{

    @Mapping(source = "standard.id", target = "standardId")
    @Mapping(source = "reportCard.id", target = "reportCardId")
    @Mapping(source = "reportCard.exam.name", target = "examName")
    @Mapping(target = "standardName", expression = "java(getStandardName(standardReport.getStandard()))")
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

    default String getStandardName(Standard standard) {
        if(standard == null) {
            return null;
        }
        return standard.getGrade()+"_"+standard.getSection();
    }

}
