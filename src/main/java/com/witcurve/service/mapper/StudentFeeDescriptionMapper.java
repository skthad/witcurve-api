package com.witcurve.service.mapper;

import com.witcurve.domain.StudentFeeDescription;
import com.witcurve.service.dto.StudentFeeDescriptionDTO;
import org.mapstruct.Mapping;

public interface StudentFeeDescriptionMapper extends EntityMapper<StudentFeeDescriptionDTO, StudentFeeDescription> {

    @Mapping(source = "feeDescriptionId", target = "feeDescription.id")
    StudentFeeDescription toEntity(StudentFeeDescriptionDTO studentFeeDescriptionDTO);


    @Mapping(target = "feeDescriptionId", source = "feeDescription.id")
    StudentFeeDescriptionDTO toDto(StudentFeeDescription studentFeeDescription);

    default StudentFeeDescription fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudentFeeDescription studentFeeDescription = new StudentFeeDescription();
        studentFeeDescription.setId(id);
        return studentFeeDescription;
    }
}
