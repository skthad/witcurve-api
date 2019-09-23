package com.witcurve.service.mapper;

import com.witcurve.domain.Attribute;
import com.witcurve.domain.ReportCardDesign;
import com.witcurve.service.dto.ReportCardDesignDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.*;

@Mapper(componentModel = "spring", uses = {ExamMapper.class, CourseMapper.class, AttributeMapper.class})
public interface ReportCardDesignMapper extends EntityMapper<ReportCardDesignDTO, ReportCardDesign> {

    @Mapping(source = "exam.id", target = "examId")
    @Mapping(source = "courses", target = "courseDTOs")
    @Mapping(source = "attributes", target = "attributeDTOs")
    @Mapping(target = "attributeTitles", expression = "java(getTitles(reportCardDesign.getAttributes()))")
    @Mapping(target = "attributeTitleColumnMap", expression = "java(getTitleColumnMap(reportCardDesign.getAttributes()))")
    ReportCardDesignDTO toDto(ReportCardDesign reportCardDesign);

    @Mapping(source = "examId", target = "exam")
    @Mapping(source = "courseDTOs", target = "courses")
    @Mapping(source = "attributeDTOs", target = "attributes")
    ReportCardDesign toEntity(ReportCardDesignDTO reportCardDesignDTO);

    default ReportCardDesign fromId(Long id) {
        if(id == null) {
            return null;
        }
        ReportCardDesign reportCardDesign = new ReportCardDesign();
        reportCardDesign.setId(id);
        return reportCardDesign;
    }

    default List<String> getTitles(List<Attribute> attributeList) {
        if(attributeList == null || attributeList.size()==0) {
            return null;
        } else {
            Map<Integer, String> result = new HashMap<>();
            for(Attribute attribute : attributeList) {
                result.put(attribute.getTitleOrder(), attribute.getTitle());
            }
            return new ArrayList<>(result.values());
        }

    }

    default Map<String, Map<Integer, String>> getTitleColumnMap(List<Attribute> attributeList) {
        if(attributeList == null || attributeList.size()==0) {
            return null;
        } else {
            Map<String, Map<Integer, String>> result = new HashMap<>();
            for(Attribute attribute : attributeList) {
                Map<Integer, String> columnMap;
                if(result.get(attribute.getTitle()) == null) {
                    columnMap = new HashMap<>();
                } else {
                    columnMap = result.get(attribute.getTitle());
                }
                columnMap.put(attribute.getColumnOrder(), attribute.getColumn());
                result.put(attribute.getTitle(), columnMap);
            }
            return result;
        }
    }
}
