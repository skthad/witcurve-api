package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.domain.Student;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.service.StudentService;
import com.witcurve.service.dto.StudentDTO;
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
        StudentDTO studentDTO = studentMapperLite.toDto(student);
        return studentDTO;
    }

    @Override
    public List<StudentDTO> getStudentsBySchoolId(Long schoolId) {
        log.debug("Request to get students with school id : {} ", schoolId);
        List<Student> studentList = studentRepository.findBySchoolId(schoolId);
        List<StudentDTO> result = studentMapper.toDto(studentList);
        return result;
    }

    @Override
    public StudentDTO getStudentByUserId(Long userId) throws WitcurveException {
        log.debug("Request to get students with user id : {}", userId);
        Student student = studentRepository.getStudentByUserId(userId);
        return studentMapper.toDto(student);
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

    @Override
    public StudentDTO getStudentBySchoolIdAndAdmissionId(Long schoolId, String admissionId) throws WitcurveException {
        log.debug("Request to get student with school id : {} and admission id : {}", schoolId, admissionId);
        Student student = studentRepository.findBySchoolIdAndAdmissionId(schoolId, admissionId.toLowerCase());
        if (student == null){
            throw  new WitcurveException("No student with given admission id in the give school id");
        }
        StudentDTO result = studentMapper.toDto(student);
        if (Strings.isNullOrEmpty(student.getUser().getPassword())) {
            result.setHasPassword(Boolean.FALSE);
        } else {
            result.setHasPassword(Boolean.TRUE);
        }
        return result;
    }
}
