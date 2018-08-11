package com.witcurve.service.impl;

import com.witcurve.domain.Student;
import com.witcurve.repository.StudentRepository;
import com.witcurve.service.StudentService;
import com.witcurve.service.dto.StudentDTO;
import com.witcurve.service.mapper.StudentMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private final Logger log  = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentMapper studentMapper;

    @Override
    public StudentDTO saveOrUpdate(StudentDTO studentDTO) {
        log.debug("Request to save or update student : {}", studentDTO);
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        student = studentRepository.save(student);
        return studentMapper.studentToStudentDTO(student);
    }

    @Override
    public StudentDTO getStudentById(Long studentId) throws WitcurveException {
        log.debug("Request to get student with id : {}", studentId);
        Student student = studentRepository.findById(studentId).get();
        if (student ==  null) {
            throw new WitcurveException("No student with given id");
        }
        return studentMapper.studentToStudentDTO(student);
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
