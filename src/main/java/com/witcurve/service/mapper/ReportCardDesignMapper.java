package com.witcurve.service.mapper;

import com.witcurve.domain.ReportCardDesign;
import com.witcurve.service.dto.ReportCardDesignDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ExamMapper.class})
public interface ReportCardDesignMapper extends EntityMapper<ReportCardDesignDTO, ReportCardDesign> {

    @Mapping(source = "exam.id", target = "examId")
    ReportCardDesignDTO toDto(ReportCardDesign reportCardDesign);

    @Mapping(source = "examId", target = "exam")
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
