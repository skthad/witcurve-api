package com.witcurve.service.mapper;

import com.witcurve.domain.ReportCard;
import com.witcurve.service.dto.ReportCardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ExamMapper.class, CourseMapper.class, ReportCardDesignMapper.class, ScholasticReportDetailsMapper.class, NonScholasticReportDetailsMapper.class})
public interface ReportCardMapper extends EntityMapper<ReportCardDTO, ReportCard> {

    @Mapping(source = "exam.id", target = "examId")
    @Mapping(source = "nonScholasticReportDetails", target = "nonScholasticDetails")
    ReportCardDTO toDto(ReportCard reportCard);

    @Mapping(source = "examId", target = "exam")
    @Mapping(source = "nonScholasticDetails", target = "nonScholasticReportDetails")
    ReportCard toEntity(ReportCardDTO reportCardDTO);

    default ReportCard fromId(Long id) {
        if(id == null) {
            return null;
        }
        ReportCard reportCard = new ReportCard();
        reportCard.setId(id);
        return reportCard;
    }

}
