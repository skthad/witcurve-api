package com.witcurve.service.mapper;

import com.witcurve.domain.StudentReport;
import com.witcurve.service.dto.StudentReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentMapperLite.class, ReportCardMapper.class})
public interface StudentReportMapper extends EntityMapper<StudentReportDTO, StudentReport> {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "reportCard.id", target = "reportCardId")
    StudentReportDTO toDto(StudentReport studentReport);

    @Mapping(target = "student", source = "studentId")
    @Mapping(target = "reportCard", source = "reportCardId")
    StudentReport toEntity(StudentReportDTO studentReportDTO);

    default StudentReport fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudentReport studentReport = new StudentReport();
        studentReport.setId(id);
        return studentReport;
    }
}
