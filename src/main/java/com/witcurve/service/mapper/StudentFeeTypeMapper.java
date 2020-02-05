package com.witcurve.service.mapper;

import com.witcurve.domain.StudentFeeDescription;
import com.witcurve.domain.StudentFeeType;
import com.witcurve.service.dto.StudentFeeTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FeeDetailsMapper.class, StudentFeeDescriptionMapper.class})
public interface StudentFeeTypeMapper extends EntityMapper<StudentFeeTypeDTO, StudentFeeType> {

    @Mapping(source = "feeTypeId", target = "feeType.id")
    StudentFeeType toEntity(StudentFeeTypeDTO studentFeeTypeDTO);


    @Mapping(target = "feeTypeId", source = "feeType.id")
    @Mapping(target = "amount", expression = "java(getTotalFeeDescriptionAmount(studentFeeType.getStudentFeeDescriptions()))")
    @Mapping(target = "feeTypeName", source = "feeType.name")
    StudentFeeTypeDTO toDto(StudentFeeType studentFeeType);

    default StudentFeeType fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudentFeeType studentFeeType = new StudentFeeType();
        studentFeeType.setId(id);
        return studentFeeType;
    }

    default Double getTotalFeeDescriptionAmount(List<StudentFeeDescription> feeDescriptions) {
        if (feeDescriptions == null || feeDescriptions.size() == 0) {
            return 0.0;
        }
        Double amount = 0.0;
        for (StudentFeeDescription feeDescription : feeDescriptions) {
            Double feeDescriptionAmt = feeDescription.getAmount() + feeDescription.getAdjustment();
            amount = amount + feeDescriptionAmt;
        }
        return amount;
    }
}
