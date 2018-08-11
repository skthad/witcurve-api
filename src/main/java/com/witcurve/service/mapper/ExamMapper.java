package com.witcurve.service.mapper;

import com.witcurve.domain.Exam;
import com.witcurve.service.dto.ExamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    @Mapping(source = "academicSessionId", target = "academicSession.id")
    Exam examDTOToExam(ExamDTO examDTO);

    @Mapping(target = "academicSessionId", source = "academicSession.id")
    ExamDTO examToExamDTO(Exam exam);

    List<Exam> examDTOsToExams(List<ExamDTO> examDTOS);

    List<ExamDTO> examsToExamDTOs(List<Exam> exams);
}
