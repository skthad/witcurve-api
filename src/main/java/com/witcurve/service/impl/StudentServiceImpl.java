package com.witcurve.service.impl;

import com.witcurve.domain.Student;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.service.StudentService;
import com.witcurve.service.dto.StudentDTO;
import com.witcurve.service.dto.UserDTO;
import com.witcurve.service.mapper.StudentMapper;
import com.witcurve.service.mapper.StudentMapperLite;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final Logger log  = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    StudentMapperLite studentMapperLite;

    @Override
    public StudentDTO saveOrUpdate(StudentDTO studentDTO) {
        log.debug("Request to save or update student : {}", studentDTO);
        Student student = studentMapper.toEntity(studentDTO);
        student = studentRepository.save(student);
        return studentMapperLite.toDto(student);
    }

    @Override
    public StudentDTO getStudentById(Long studentId) throws WitcurveException {
        log.debug("Request to get student with id : {}", studentId);
        Student student = studentRepository.findById(studentId).get();
        if (student ==  null) {
            throw new WitcurveException("No student with given id");
        }
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(student.getUser().getId());
        StudentDTO studentDTO = studentMapperLite.toDto(student);
//        studentDTO.setUser(userDTO);
        return studentDTO;
    }

    @Override
    public List<StudentDTO> getStudentsByStandardId(Long standardId) throws WitcurveException {
        log.debug("Request to get students with standard id : {}", standardId);
        List<Student> students = studentStandardRepository.getStudentsByStandardId(standardId);
        if (students ==  null || students.size() == 0) {
            throw new WitcurveException("No students in the given standard id");
        }
        return studentMapperLite.toDto(students);
    }

    @Override
    public void deleteStudent(Long studentId) throws WitcurveException {
        log.debug("Request to delete student with id : {}", studentId);
        Student student = studentRepository.findById(studentId).get();
        if (student == null){
            throw  new WitcurveException("No student with given id");
        }
        studentRepository.delete(student);
    }
}
