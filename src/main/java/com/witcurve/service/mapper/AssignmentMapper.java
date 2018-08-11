package com.witcurve.service.mapper;

import com.witcurve.domain.Assignment;
import com.witcurve.service.dto.AssignmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {

    @Mapping(source = "timeTableUnitId", target = "timeTableUnit.id")
    Assignment assignmentDTOToAssignment(AssignmentDTO assignmentDTO);

    @Mapping(target = "timeTableUnitId", source = "timeTableUnit.id")
    AssignmentDTO assignmentToAssignmentDTO(Assignment assignment);

    List<Assignment> assignmentDTOsToAssignments(List<AssignmentDTO> assignmentDTOS);

    List<AssignmentDTO> assignmentToAssignmentDTOs(List<Assignment> assignments);
}
