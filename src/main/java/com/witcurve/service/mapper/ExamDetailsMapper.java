package com.witcurve.service.mapper;

import com.witcurve.domain.ExamDetails;
import com.witcurve.service.dto.ExamDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamDetailsMapper {

    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "classId", target = "standard.id")
    @Mapping(source = "examId", target = "exam.id")
    ExamDetails examDetailsDTOToExamDetails(ExamDetailsDTO examDetailsDTO);

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "classId", source = "standard.id")
    @Mapping(target = "examId", source = "exam.id")
    ExamDetailsDTO examDetailsToExamDetailsDTO(ExamDetails examDetails);

    List<ExamDetails> examDetailsDTOsListToExamDetailsList(List<ExamDetailsDTO> examDetailsDTOS);

    List<ExamDetailsDTO> examDetailsListToExamDetailsDTOsList(List<ExamDetails> examDetails);
}
