package com.witcurve.service.mapper;

import com.witcurve.domain.StudentFeeStructure;
import com.witcurve.service.dto.StudentFeeStructureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AcademicSessionMapper.class, StudentMapper.class,StudentFeeTypeMapper.class})
public interface StudentFeeStructureMapper extends EntityMapper<StudentFeeStructureDTO, StudentFeeStructure> {

    @Mapping(source = "sessionId", target = "session.id")
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "selectedSessionId", target = "selectedSession")
    StudentFeeStructure toEntity(StudentFeeStructureDTO studentFeeStructureDTO);


    @Mapping(target = "sessionId", source = "session.id")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "selectedSessionId", source = "selectedSession.id")
    StudentFeeStructureDTO toDto(StudentFeeStructure studentFeeStructure);

    default StudentFeeStructure fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudentFeeStructure studentFeeStructure = new StudentFeeStructure();
        studentFeeStructure.setId(id);
        return studentFeeStructure;
    }
}
