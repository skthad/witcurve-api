package com.witcurve.service.mapper;

import com.witcurve.domain.Leave;
import com.witcurve.service.dto.LeaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeaveMapper {

    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "approverId", target = "approvedBy.id")
    Leave leaveDTOToLeave(LeaveDTO leaveDTO);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "approverId", source = "approvedBy.id")
    LeaveDTO leaveToLeaveDTO(Leave leave);

    List<Leave> leaveDTOsToLeaves(List<LeaveDTO> leaveDTOS);

    List<LeaveDTO> leavesToLeaveDTOs(List<Leave> leaves);
}
