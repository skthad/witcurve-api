package com.witcurve.service.mapper;

import com.witcurve.domain.LeaveApplication;
import com.witcurve.service.dto.LeaveApplicationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StaffMapper.class ,GuardianMapper.class, EventMapper.class, StudentMapperLite.class , AcademicSessionMapper.class})
public interface LeaveApplicationMapper extends EntityMapper<LeaveApplicationDTO, LeaveApplication> {

    @Mapping(target = "appliedStaffId", source = "appliedStaff.id")
    @Mapping(target= "appliedGuardianId", source="appliedGuardian.id" )
    @Mapping(target= "appliedStudentId",source="appliedStudent.id")
    @Mapping(target= "eventId", source="event.id" )
    @Mapping(target= "approvedById", source="approvedBy.id" )
    @Mapping(target="sessionId", source="session.id")
    LeaveApplicationDTO toDto(LeaveApplication leaveApplication);

    @Mapping(target = "appliedStaff", source = "appliedStaffId")
    @Mapping(target = "appliedGuardian", source = "appliedGuardianId")
    @Mapping(target="appliedStudent" , source="appliedStudentId")
    @Mapping(target= "event", source="eventId" )
    @Mapping(target= "approvedBy", source="approvedById" )
    @Mapping(target = "session", source = "sessionId")
    LeaveApplication toEntity(LeaveApplicationDTO leaveApplicationDTO);

    default LeaveApplication fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveApplication leaveApplication = new LeaveApplication();
        leaveApplication.setId(id);
        return leaveApplication;
    }

}
