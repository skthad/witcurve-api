package com.witcurve.service.mapper;

import com.witcurve.domain.StudentFeeDescription;
import com.witcurve.domain.StudentFeeStructure;
import com.witcurve.domain.StudentFeeType;
import com.witcurve.service.dto.StudentFeeStructureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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
 //   @Mapping(target = "amount", expression = "java(getTotalFeeTypeAmount(studentFeeStructure.getStudentFeeTypes()))")
  //  @Mapping(target = "totalOneTimeDiscount", expression = "java(getTotalOneTimeDiscount(studentFeeStructure.getStudentFeeTypes()))")
    StudentFeeStructureDTO toDto(StudentFeeStructure studentFeeStructure);

    default StudentFeeStructure fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudentFeeStructure studentFeeStructure = new StudentFeeStructure();
        studentFeeStructure.setId(id);
        return studentFeeStructure;
    }

    default Double getTotalOneTimeDiscount(List<StudentFeeType> feeTypes){
        if (feeTypes == null || feeTypes.size() == 0) {
            return 0.0;
        }
        Double totalOneTimeDiscount = 0.0;
        for (StudentFeeType feeType : feeTypes) {
            Double totalFeeDescriptionDiscount = 0.0;
            if (feeType.getStudentFeeDescriptions() != null || feeType.getStudentFeeDescriptions().size() > 0) {
                for (StudentFeeDescription feeDescription : feeType.getStudentFeeDescriptions()) {
                    totalFeeDescriptionDiscount = totalFeeDescriptionDiscount + feeDescription.getOneTimeDiscount();
                }
                totalOneTimeDiscount = totalOneTimeDiscount + totalFeeDescriptionDiscount;
            }
        }

        return totalOneTimeDiscount;
    }
}
