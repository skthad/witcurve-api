package com.witcurve.service.mapper;

import com.witcurve.domain.StudentRemarks;
import com.witcurve.service.dto.StudentRemarksDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={StudentMapperLite.class, ExamMapper.class})
public interface StudentRemarksMapper extends EntityMapper<StudentRemarksDTO, StudentRemarks> {

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "examId", source = "exam.id")
    StudentRemarksDTO toDto(StudentRemarks studentRemarks);

    @Mapping(target = "student", source = "studentId")
    @Mapping(target = "exam", source = "examId")
    StudentRemarks toEntity(StudentRemarksDTO studentRemarksDTO);

    default StudentRemarks fromId(Long id) {
        if(id == null) {
            return null;
        }
        StudentRemarks studentRemarks = new StudentRemarks();
        studentRemarks.setId(id);
        return studentRemarks;
    }
}
