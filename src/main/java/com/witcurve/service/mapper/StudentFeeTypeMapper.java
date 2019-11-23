package com.witcurve.service.mapper;

import com.witcurve.domain.StudentFeeType;
import com.witcurve.service.dto.StudentFeeTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FeeDetailsMapper.class,StudentFeeDescriptionMapper.class})
public interface StudentFeeTypeMapper extends EntityMapper<StudentFeeTypeDTO, StudentFeeType> {

    @Mapping(source = "feeTypeId", target = "feeType.id")
    StudentFeeType toEntity(StudentFeeTypeDTO studentFeeTypeDTO);


    @Mapping(target = "feeTypeId", source = "feeType.id")
    StudentFeeTypeDTO toDto(StudentFeeType studentFeeType);

    default StudentFeeType fromId(Long id) {
        if (id == null) {
            return null;
        }
        StudentFeeType studentFeeType = new StudentFeeType();
        studentFeeType.setId(id);
        return studentFeeType;
    }
}
