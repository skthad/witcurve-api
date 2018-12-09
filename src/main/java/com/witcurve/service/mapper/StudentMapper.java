package com.witcurve.service.mapper;

import com.witcurve.domain.Student;
import com.witcurve.domain.User;
import com.witcurve.service.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SchoolInfoMapper.class})
public interface StudentMapper extends EntityMapper<StudentDTO, Student>{

    @Mapping(target = "userId", source = "user.id")
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

    default User fromUserId(Long id) {
        if(id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return  user;
    }
}
