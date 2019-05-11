package com.witcurve.service.mapper;

import com.witcurve.domain.Student;
import com.witcurve.domain.StudentStandard;
import com.witcurve.domain.User;
import com.witcurve.service.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface StudentMapper extends EntityMapper<StudentDTO, Student>{

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.login")
    @Mapping(target = "active", source = "user.activated")
    @Mapping(target = "rollNo", expression = "java(getStudentRollNo(student.getStudentStandards()))")
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
