package com.witcurve.service.mapper;

import com.witcurve.domain.Event;
import com.witcurve.domain.LeaveApplication;
import com.witcurve.service.dto.LeaveApplicationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {StaffMapper.class ,GuardianMapper.class, EventMapper.class, StudentMapperLite.class , AcademicSessionMapper.class, EventMapper.class})
public interface LeaveApplicationMapper extends EntityMapper<LeaveApplicationDTO, LeaveApplication> {

    @Mapping(target = "appliedStaffId", source = "appliedStaff.id")
    @Mapping(target= "appliedGuardianId", source="appliedGuardian.id" )
    @Mapping(target= "appliedStudentId",source="appliedStudent.id")
    @Mapping(target= "approvedById", source="approvedBy.id" )
    @Mapping(target="sessionId", source="session.id")
    @Mapping(target = "eventIds" , expression = "java(getIdsFromEvents(leaveApplication))")
    LeaveApplicationDTO toDto(LeaveApplication leaveApplication);

    @Mapping(target = "appliedStaff", source = "appliedStaffId")
    @Mapping(target = "appliedGuardian", source = "appliedGuardianId")
    @Mapping(target="appliedStudent" , source="appliedStudentId")
    @Mapping(target= "approvedBy", source="approvedById" )
    @Mapping(target = "session", source = "sessionId")
    @Mapping(target="events",ignore=true)
    LeaveApplication toEntity(LeaveApplicationDTO leaveApplicationDTO);

    default List<Long> getIdsFromEvents(LeaveApplication leaveApplication) {
        List<Event> eventList = new ArrayList<>(leaveApplication.getEvents());
        List<Long> result = new ArrayList<>();
        if(eventList == null) {
            return null;
        }
        for(Event e : eventList) {
            result.add(e.getId());
        }
        return result;
    }

    default LeaveApplication fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveApplication leaveApplication = new LeaveApplication();
        leaveApplication.setId(id);
        return leaveApplication;
    }

}
