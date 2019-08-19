package com.witcurve.service.mapper;

import com.witcurve.domain.ReportCardDesign;
import com.witcurve.service.dto.ReportCardDesignDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface ReportCardDesignMapper extends EntityMapper<ReportCardDesignDTO, ReportCardDesign> {

    @Mapping(source = "schoolInfo.id", target = "schoolInfoId")
    ReportCardDesignDTO toDto(ReportCardDesign reportCardDesign);

    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    ReportCardDesign toEntity(ReportCardDesignDTO reportCardDesignDTO);

    default ReportCardDesign fromId(Long id) {
        if(id == null) {
            return null;
        }
        ReportCardDesign reportCardDesign = new ReportCardDesign();
        reportCardDesign.setId(id);
        return reportCardDesign;
    }
}
