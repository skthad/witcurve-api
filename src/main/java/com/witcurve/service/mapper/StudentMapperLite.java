package com.witcurve.service.mapper;

import com.witcurve.domain.School;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.Student;
import com.witcurve.domain.User;
import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.service.dto.SchoolInfoDTO;
import com.witcurve.service.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface StudentMapperLite extends EntityMapper<StudentDTO, Student>{

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.login")
    @Mapping(target = "active", source = "user.activated")
    StudentDTO toDto(Student student);

    @Mapping(target = "user.id", source = "userId")
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
}
