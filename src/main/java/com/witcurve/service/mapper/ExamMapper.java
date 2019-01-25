package com.witcurve.service.mapper;

import com.witcurve.domain.Exam;
import com.witcurve.service.dto.ExamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {

    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    Exam toEntity(ExamDTO examDTO);

    @Mapping(target = "schoolInfoId", source = "schoolInfo.id")
    ExamDTO toDto(Exam exam);


    default Exam fromId(Long id) {
        if(id == null) {
            return null;
        }
        Exam exam = new Exam();
        exam.setId(id);
        return exam;
    }
}
