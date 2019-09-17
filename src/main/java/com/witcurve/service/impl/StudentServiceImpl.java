package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.SubscriptionModel;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.*;
import com.witcurve.service.StudentService;
import com.witcurve.service.StudentStandardService;
import com.witcurve.service.UserService;
import com.witcurve.service.dto.StudentDTO;
import com.witcurve.service.dto.UserDTO;
import com.witcurve.service.mapper.StudentMapper;
import com.witcurve.service.mapper.StudentMapperLite;
import com.witcurve.service.mapper.UserMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    CourseRepository courseRepository;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    StudentMapperLite studentMapperLite;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    StudentCourseRepository studentCourseRepository;

    @Autowired
    StudentStandardService studentStandardService;

    @Override
    public StudentDTO create(StudentDTO studentDTO) {
        log.debug("Request to create student : {}", studentDTO);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(studentDTO.getEmail());
        userDTO.setLogin(studentDTO.getSchoolInfo().getId() + "-" + studentDTO.getAdmissionId().toLowerCase());
        userDTO.setFirstName(studentDTO.getFirstName());
        userDTO.setLastName(studentDTO.getLastName());
        userDTO.setType(UserType.PARENT);
        User user = userService.createUser(userDTO);

        populateSubscriptionFields(studentDTO);
        Student student = studentMapper.toEntity(studentDTO);
        student.setUser(user);
        student = studentRepository.save(student);
        return studentMapperLite.toDto(student);
    }

    @Override
    public StudentDTO update(StudentDTO studentDTO) throws WitcurveException {
        log.debug("Request to update student : {}", studentDTO);
        Optional<User> optionalUser = userRepository.findById(studentDTO.getUserId());
        if(!optionalUser.isPresent()) {
            throw new WitcurveException("There is no user with given id : "+studentDTO.getUserId());
        }
        UserDTO userDTO = userMapper.userToUserDTO(optionalUser.get());
        userDTO.setLogin(studentDTO.getSchoolInfo().getId() + "-" + studentDTO.getAdmissionId().toLowerCase());
        userDTO.setFirstName(studentDTO.getFirstName());
        userDTO.setLastName(studentDTO.getLastName());
        userDTO.setEmail(studentDTO.getEmail());
        userDTO.setType(UserType.PARENT);
        userService.updateUser(userDTO);

        populateSubscriptionFields(studentDTO);
        Student student = studentMapper.toEntity(studentDTO);
        student = studentRepository.save(student);
        return studentMapperLite.toDto(student);
    }

    private void populateSubscriptionFields(StudentDTO studentDTO) {
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(studentDTO.getSchoolInfo().getId());
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No SchoolInfo with given id " + studentDTO.getSchoolInfo().getId());
        }

        if (schoolInfo.get().getSchool().getInstitute().getSubscriptionModel() == SubscriptionModel.INSTITUTE) {
            studentDTO.setSubscriptionStartDate(schoolInfo.get().getSchool().getInstitute().getSubscriptionStartDate());
            studentDTO.setSubscriptionEndDate(schoolInfo.get().getSchool().getInstitute().getSubscriptionEndDate());
        }
    }

    @Override
    public StudentDTO getStudentById(Long studentId) throws WitcurveException {
        log.debug("Request to get student with id : {}", studentId);
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new WitcurveException("No student with given id");
        }
        StudentDTO studentDTO = studentMapperLite.toDto(student.get());
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
    public List<StudentDTO> getUnAllocatedStudentsBySchoolInfoId(Long schoolInfoId) {
        log.debug("Request to get unallocated students in schoolInfo with id : {}", schoolInfoId);
        List<Student> students = studentRepository.getUnallocatedStudentsBySchoolInfoId(schoolInfoId);
        return studentMapperLite.toDto(students);
    }

    @Override
    public List<StudentDTO> getInActiveStudentsBySchoolInfoId(Long schoolInfoId) {
        log.debug("Request to get in-active students in schoolInfo with id : {}", schoolInfoId);
        List<Student> students = studentRepository.getInactiveStudentsBySchoolInfoId(schoolInfoId);
        return studentMapperLite.toDto(students);
    }

    @Override
    public StudentDTO getStudentByUsername(String username) throws WitcurveException {
        log.info("Request to get student with username: {}", username);
        int index = username.indexOf("-");
        if(index > 0) {
            Long schoolInfoId;
            try {
                schoolInfoId = Long.parseLong(username.substring(0, index));
            } catch (NumberFormatException e) {
                log.error("Entered school info id in user name is wrong : {}", username);
                throw new WitcurveException("Invalid username, please enter the correct username");
            }
            String admissionId = username.substring(index + 1);
            Student student = studentRepository.findBySchoolInfoIdAndAdmissionId(schoolInfoId, admissionId.toLowerCase());
            if (student == null){
                log.error("No student with given admission id : {} in the give school info id : {}", admissionId, schoolInfoId);
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
    public void deactivate(Long studentId) throws WitcurveException {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            studentStandardService.deactivateStudentStandard(Arrays.asList(studentId));
            student.get().getUser().setActivated(false);
        } else {
            throw new WitcurveException("No student with given id");
        }
    }

    @Override
    public void activate(Long studentId) throws WitcurveException {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            student.get().getUser().setActivated(true);
        } else {
            throw new WitcurveException("No student with given id");
        }
    }

    @Override
    public void mapStudentsInNonElectiveCourses(StudentStandard studentStandard) {
        mapStudentsInNonElectiveCourses(Arrays.asList(studentStandard), studentStandard.getStandard().getId());
    }

    @Override
    public void mapStudentsInNonElectiveCourses(List<StudentStandard> studentStandards, Long standardId) {
        Optional<Standard> standard = standardRepository.findById(standardId);
        if (!standard.isPresent()) {
            throw new WitcurveException("No standard found with ID: " + standard);
        }
        List<Course> courses = courseRepository.findNonElectivesBySchoolInfo(standard.get().getSchoolInfo().getId());
        List<StudentCourse> coursesToMap = null;
        for (StudentStandard studentStandard: studentStandards) {
            for (Course course: courses) {
                if (coursesToMap == null) {
                    coursesToMap = new ArrayList<>();
                }
                StudentCourse sc = new StudentCourse();
                sc.setStudentStandard(studentStandard);
                sc.setCourse(course);
                coursesToMap.add(sc);
            }
        }
        if (coursesToMap != null) {
            studentCourseRepository.saveAll(coursesToMap);
        }
    }

    @Override
    public void mapUnmapStudentAndCourse(Long studentStandardId, Long courseId, Boolean map) {
        Optional<StudentStandard> ss = studentStandardRepository.findById(studentStandardId);
        if (!ss.isPresent()) {
            throw new WitcurveException("Given studentStandardId does not exist");
        }
        Optional<Course> course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new WitcurveException("Given courseId does not exist");
        }
        if (map) {
            StudentCourse sc = new StudentCourse();
            sc.setStudentStandard(ss.get());
            sc.setCourse(course.get());
            studentCourseRepository.save(sc);
        } else {
            studentCourseRepository.unmapStudentCourse(studentStandardId, courseId);
        }
    }

    @Override
    public void mapOneTime(Long schoolInfoId) {
        List<StudentStandard> studentStandards = studentStandardRepository.getBySchoolInfoId(schoolInfoId);
        List<Course> courses = courseRepository.findNonElectivesBySchoolInfo(schoolInfoId);
        Map<Grade, List<Course>> gradeCourseMap = new HashMap<>();
        for (Course course: courses) {
            if (gradeCourseMap.get(course.getGrade()) == null) {
                gradeCourseMap.put(course.getGrade(), new ArrayList<>());
            }
            gradeCourseMap.get(course.getGrade()).add(course);
        }
        List<StudentCourse> studentCourses = null;
        for (StudentStandard ss: studentStandards) {
            if (studentCourses == null) {
                studentCourses = new ArrayList<>();
            }
            List<Course> coursesToMap = gradeCourseMap.get(ss.getStandard().getGrade());
            if (coursesToMap == null) {
                continue;
            }
            for (Course courseToMap: coursesToMap) {
                StudentCourse sc = new StudentCourse();
                sc.setStudentStandard(ss);
                sc.setCourse(courseToMap);
                studentCourses.add(sc);
            }
        }
        if (studentCourses != null) {
            studentCourseRepository.saveAll(studentCourses);
        }
    }
}
