package com.witcurve.service.mapper;

import com.witcurve.domain.Course;
import com.witcurve.domain.MasterSubject;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.service.dto.CourseDTO;
import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.service.dto.SchoolInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SchoolInfoMapper.class)
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {

    @Mapping(source = "masterSubject.name", target = "masterSubject")
    @Mapping(target = "schoolInfoId", source = "schoolInfo.id")
    CourseDTO toDto(Course course);

    @Mapping(target = "masterSubject.name", source = "masterSubject")
    @Mapping(source = "schoolInfoId", target = "schoolInfo")
    Course toEntity(CourseDTO courseDTO);

    default Course fromId(Long id) {
        if(id == null) {
            return null;
        }
        Course course = new Course();
        course.setId(id);
        return course;
    }

    default MasterSubject masterSubjectFromName(String name) {
        if(name == null) {
            return null;
        }
        MasterSubject masterSubject = new MasterSubject();
        masterSubject.setName(name);
        return masterSubject;
    }

    default SchoolInfoDTO toSchoolInfoDTO(SchoolInfo schoolInfo) {
        SchoolInfoDTO schoolInfoDTO = new SchoolInfoDTO();
        schoolInfoDTO.setId(schoolInfo.getId());

        SchoolDTO school = new SchoolDTO();
        school.setId(schoolInfo.getSchool().getId());
        schoolInfoDTO.setSchool(school);

        return schoolInfoDTO;
    }

}
