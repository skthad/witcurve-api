package com.witcurve.service.mapper;

import com.witcurve.domain.*;
import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.service.dto.SchoolInfoDTO;
import com.witcurve.service.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StudentMapperLite extends EntityMapper<StudentDTO, Student>{

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.login")
    @Mapping(target = "rollNo", expression = "java(getStudentRollNo(student.getStudentStandards()))")
    @Mapping(ignore = true, target = "active")
    @Mapping(ignore = true, target = "hasPassword")
    StudentDTO toDto(Student student);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(ignore = true, target = "studentStandards")
    Student toEntity(StudentDTO studentDTO);

    default Student fromId(Long id) {
        if(id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return  student;
    }

    default SchoolInfo toSchoolInfo(SchoolInfoDTO schoolInfoDTO) {
        if(schoolInfoDTO != null) {
            SchoolInfo schoolInfo = new SchoolInfo();
            schoolInfo.setId(schoolInfoDTO.getId());
            if(schoolInfo.getSchool() != null) {
                School school = new School();
                school.setId(schoolInfoDTO.getSchool().getId());
                schoolInfo.setSchool(school);
            }
            return schoolInfo;
        }
        return null;
    }

    default SchoolInfoDTO toSchoolInfoDTO(SchoolInfo schoolInfo) {
        if(schoolInfo != null) {
            SchoolInfoDTO schoolInfoDTO = new SchoolInfoDTO();
            schoolInfoDTO.setId(schoolInfo.getId());
            if(schoolInfoDTO.getSchool() != null) {
                SchoolDTO school = new SchoolDTO();
                school.setId(schoolInfo.getSchool().getId());
                schoolInfoDTO.setSchool(school);
            }
            return schoolInfoDTO;
        }
        return null;
    }

    default User fromUserId(Long id) {
        if(id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return  user;
    }

    default String getStudentRollNo(Set<StudentStandard> studentStandards) {
        if(studentStandards != null && studentStandards.size()!=0) {
            return (new ArrayList<>(studentStandards)).get(0).getRollNo();
        }
        return null;
    }
}
