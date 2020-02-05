package com.witcurve.service.mapper;

import com.witcurve.domain.*;
import com.witcurve.service.dto.StudentFeeStructureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AcademicSessionMapper.class, StudentMapper.class, StudentFeeTypeMapper.class, StudentFeeDescriptionMapper.class})
public interface StudentFeeStructureMapper extends EntityMapper<StudentFeeStructureDTO, StudentFeeStructure> {

    @Mapping(source = "sessionId", target = "session.id")
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "selectedSessionId", target = "selectedSession")
    @Mapping(ignore = true, target = "feePaymentRecords")
    StudentFeeStructure toEntity(StudentFeeStructureDTO studentFeeStructureDTO);


    @Mapping(target = "sessionId", source = "session.id")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "selectedSessionId", source = "selectedSession.id")
    @Mapping(target = "admissionId", source = "student.admissionId")
    @Mapping(target = "className", expression = "java(getClassName(studentFeeStructure.getStudent()))")
    @Mapping(target = "dateOfJoining", source = "student.admissionDate" )
    StudentFeeStructureDTO toDto(StudentFeeStructure studentFeeStructure);

    default StudentFeeStructure fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudentFeeStructure studentFeeStructure = new StudentFeeStructure();
        studentFeeStructure.setId(id);
        return studentFeeStructure;
    }

    default String getClassName(Student student) {
        Set<StudentStandard> studentStandards = student.getStudentStandards();
        if (studentStandards.size() > 0) {
            StudentStandard studentStandard = studentStandards.stream().collect(Collectors.toList()).get(0);
            return studentStandard.getStandard().getGrade() + "-" + studentStandard.getStandard().getSection();
        }
        return null;
    }
}
