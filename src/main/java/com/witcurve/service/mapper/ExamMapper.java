package com.witcurve.service.mapper;

import com.witcurve.domain.AcademicSession;
import com.witcurve.domain.Exam;
import com.witcurve.service.dto.ExamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AcademicSessionMapper.class})
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {

    @Mapping(source = "academicSessionId", target = "academicSession.id")
    Exam toEntity(ExamDTO examDTO);

    @Mapping(target = "academicSessionId", source = "academicSession.id")
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
