package com.witcurve.service.mapper;

import com.witcurve.domain.Event;
import com.witcurve.domain.Staff;
import com.witcurve.domain.Student;
import com.witcurve.service.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StandardMapper.class, StaffMapper.class,
    AcademicSessionMapper.class, StudentMapperLite.class, SlotCourseDetailsMapper.class,
    CourseTeacherMapper.class, SchoolInfoMapper.class})
public interface EventMapper extends EntityMapper<EventDTO, Event>{

    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    @Mapping(source = "standardId", target = "standard")
    @Mapping(source = "staffId", target = "staff")
    @Mapping(source = "studentId", target = "student")
    Event toEntity(EventDTO eventDTO);

    @Mapping(target = "schoolInfoId", source = "schoolInfo.id")
    @Mapping(target = "standardId", source = "standard.id")
    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "staffName", expression = "java(getStaffName(event.getStaff()))")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", expression = "java(getStudentName(event.getStudent()))")
    EventDTO toDto(Event event);

    default Event fromId(Long id) {
        if (id == null) {
            return  null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
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
