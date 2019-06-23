package com.witcurve.service.mapper;

import com.witcurve.domain.*;
import com.witcurve.domain.Keyword;
import com.witcurve.service.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {StandardMapper.class, StaffMapper.class,
    AcademicSessionMapper.class, StudentMapperLite.class, SlotCourseDetailsMapper.class,
    CourseTeacherMapper.class, SchoolInfoMapper.class})
public interface EventMapper extends EntityMapper<EventDTO, Event>{

    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    @Mapping(source = "standardId", target = "standard")
    @Mapping(source = "staffId", target = "staff")
    @Mapping(source = "studentId", target = "student")
    @Mapping(target = "keywords", expression = "java(getKeywords(eventDTO.getKeywords()))")
    @Mapping(target = "eventContents", ignore = true)
    Event toEntity(EventDTO eventDTO);

    @Mapping(target = "schoolInfoId", source = "schoolInfo.id")
    @Mapping(target = "standardId", source = "standard.id")
    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "staffName", expression = "java(getStaffName(event.getStaff()))")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", expression = "java(getStudentName(event.getStudent()))")
    @Mapping(target = "standardName", expression = "java(getStandardName(event.getStandard()))")
    @Mapping(target = "keywords", expression = "java(getKeywordNames(event.getKeywords()))")
    @Mapping(target = "courseContentIds", expression = "java(getCourseContentIds(event.getEventContents()))")
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

    default String getStandardName(Standard standard) {
        String result = "";
        if(standard != null) {
            result += standard.getGrade() + " ";
            result += standard.getSection();
            return result;
        } else {
            return null;
        }
    }

    default Set<Keyword> getKeywords(List<String> keywords) {
        if(keywords == null || keywords.size()==0) {
            return null;
        }
        Set<Keyword> result = new HashSet<>();
        for(String keyword : keywords) {
            result.add(new Keyword(keyword));
        }
        return result;
    }

    default List<String> getKeywordNames(Set<Keyword> keywords) {
        if(keywords == null || keywords.size()==0) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for(Keyword keyword : keywords) {
            result.add(keyword.getName());
        }
        return result;
    }

    default List<Long> getCourseContentIds(Set<EventContent> eventContents) {
        if (CollectionUtils.isEmpty(eventContents)) {
            return null;
        }

        List<Long> result = new ArrayList<>();
        for (EventContent eventContent: eventContents) {
            result.add(eventContent.getCourseContent().getId());
        }
        return result;
    }
}
