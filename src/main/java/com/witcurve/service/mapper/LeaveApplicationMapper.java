package com.witcurve.service.mapper;

import com.witcurve.domain.Event;
import com.witcurve.domain.LeaveApplication;
import com.witcurve.domain.Staff;
import com.witcurve.domain.Student;
import com.witcurve.service.dto.LeaveApplicationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {StaffMapper.class ,GuardianMapper.class, EventMapper.class,
    StudentMapperLite.class , EventMapper.class, SchoolInfoMapper.class})
public interface LeaveApplicationMapper extends EntityMapper<LeaveApplicationDTO, LeaveApplication> {

    @Mapping(target = "appliedStaffId", source = "appliedStaff.id")
    @Mapping(target= "appliedStudentId",source="appliedStudent.id")
    @Mapping(target= "approvedById", source="approvedBy.id" )
    @Mapping(target="schoolInfoId", source="schoolInfo.id")
    @Mapping(target = "eventIds" , expression = "java(getIdsFromEvents(leaveApplication))")
    @Mapping(target = "studentName", expression = "java(getStudentName(leaveApplication.getAppliedStudent()))")
    @Mapping(target = "rollNo", expression = "java(getStudentRollNo(leaveApplication.getAppliedStudent()))")
    @Mapping(target = "staffName", expression = "java(getStaffName(leaveApplication.getAppliedStaff()))")
    @Mapping(ignore = true, target = "numLeaveDays")
    LeaveApplicationDTO toDto(LeaveApplication leaveApplication);

    @Mapping(target = "appliedStaff", source = "appliedStaffId")
    @Mapping(target="appliedStudent" , source="appliedStudentId")
    @Mapping(target= "approvedBy", source="approvedById" )
    @Mapping(target="schoolInfo", source="schoolInfoId")
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

    default String getStudentName(Student student) {
        String result = "";
        if(student != null) {
            result += student.getFirstName() + " ";
            if(student.getMiddleName() != null) {
                result += student.getMiddleName() + " ";
            }
            result += student.getLastName();
            return result;
        } else {
            return null;
        }
    }

    default String getStudentRollNo(Student student) {
        if(student != null && student.getStudentStandards() != null && student.getStudentStandards().size() != 0) {
            return (new ArrayList<>(student.getStudentStandards())).get(0).getRollNo();
        }
        return null;
    }

    default String getStaffName(Staff staff) {
        String result = "";
        if(staff != null) {
            result += staff.getFirstName() + " ";
            if(staff.getMiddleName() != null) {
                result += staff.getMiddleName() + " ";
            }
            result += staff.getLastName();
            return result;
        } else {
            return null;
        }
    }
}
