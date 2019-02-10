package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.domain.Student;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.repository.UserRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    UserRepository userRepository;

    @Override
    public StudentDTO create(StudentDTO studentDTO) {
        log.debug("Request to create student : {}", studentDTO);
        User user = new User();
        user.setLogin(studentDTO.getSchoolInfo().getId() + "-" + studentDTO.getAdmissionId());
        user.setFirstName(studentDTO.getFirstName());
        user.setLastName(studentDTO.getLastName());
        user.setType(UserType.PARENT);
        user.setActivated(false);
        user = userRepository.save(user);
        Student student = studentMapper.toEntity(studentDTO);
        student.setUser(user);
        student = studentRepository.save(student);
        return studentMapperLite.toDto(student);
    }

    @Override
    public StudentDTO update(StudentDTO studentDTO) {
        log.debug("Request to update student : {}", studentDTO);
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
    public List<StudentDTO> getStudentsBySchoolInfoId(Long schoolInfoId) {
        log.debug("Request to get unallocated students in schoolInfo with id : {}", schoolInfoId);
        List<Student> students = studentRepository.getUnallocatedStudentsBySchoolInfoId(schoolInfoId);
        return studentMapperLite.toDto(students);
    }

    @Override
    public StudentDTO getStudentByUsername(String username) throws WitcurveException {
        log.debug("Request to get student with username: {}", username);
        int index = username.indexOf("-");
        log.debug("Index value : {} ",index);
        if(index > 0) {
            Long schoolInfoId = null;
            try {
                schoolInfoId = Long.parseLong(username.substring(0, index));
            } catch (NumberFormatException e) {
                log.error("Entered school info id in user name is wrong : {}", username);
                throw new WitcurveException("Invalid username, please enter the correct username");
            }
            String admissionId = username.substring(index + 1);
            Student student = studentRepository.findBySchoolInfoIdAndAdmissionId(schoolInfoId, admissionId.toLowerCase());
            if (student == null){
                log.error("No student with given admission id : {} in the give school info id : {}", student.getId(), schoolInfoId);
                throw  new WitcurveException("No student exists with given username ");
            }

            StudentDTO result = studentMapper.toDto(student);
            if (Strings.isNullOrEmpty(student.getUser().getPassword())) {
                result.setHasPassword(Boolean.FALSE);
            } else {
                result.setHasPassword(Boolean.TRUE);
            }
            return result;
        } else {
            log.error("Entered user name is not in format of schoolInfoId-studentId for username : {}", username);
            throw new WitcurveException("Invalid username, please enter the correct username");
        }
    }

    @Override
    public void deactivate(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            studentStandardRepository.deactivateByStudentIds(Arrays.asList(studentId));
            student.get().getUser().setActivated(false);
        }
    }
}
