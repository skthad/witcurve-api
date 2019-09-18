package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.*;
import com.witcurve.service.StudentCourseService;
import com.witcurve.service.StudentService;
import com.witcurve.service.StudentStandardService;
import com.witcurve.service.dto.StudentCourseDTO;
import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.service.mapper.StudentStandardMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentStandardServiceImpl implements StudentStandardService {

    private final Logger log = LoggerFactory.getLogger(StudentStandardServiceImpl.class);

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    StudentStandardMapper studentStandardMapper;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    StudentCourseRepository studentCourseRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentCourseService studentCourseService;

    @Override
    public List<StudentStandardDTO> saveMultiple(List<StudentStandardDTO> studentStandardDTOs, Long standardId) throws WitcurveException {
        //todo refactor this code later. neglect this service for now
        List<String> rollNos = new ArrayList<>();
        //TODO to check if all student ids belong to standard school info id
        //TODO verify if this is still needed, if call single service in loop
        for (StudentStandardDTO studentStandardDTO : studentStandardDTOs) {
            studentStandardDTO.setId(null);
            studentStandardDTO.getStandard().setId(standardId);
            studentStandardDTO.setActive(Boolean.TRUE);
            rollNos.add(studentStandardDTO.getRollNo());
        }
        List<StudentStandard> existingRollNos = studentStandardRepository.getByStandardIdAndRollNos(standardId, rollNos);
        if (existingRollNos.size() != 0) {
            throw new WitcurveException("There already exists a student in given standard with entered roll no.");
        }
        List<Long> studentIds = studentStandardDTOs.stream().map(s -> s.getStudent().getId()).collect(Collectors.toList());
        studentStandardRepository.deactivateByStudentIds(studentIds);
        List<StudentStandard> studentStandards = studentStandardRepository.saveAll(
            studentStandardMapper.toEntity(studentStandardDTOs));

        for (StudentStandard ss : studentStandards) {
            studentService.mapStudentsInNonElectiveCourses(ss);
        }
        return studentStandardMapper.toDto(studentStandards);
    }

    @Override
    public StudentStandardDTO save(StudentStandardDTO studentStandardDTO) throws WitcurveException {
        Optional<Standard> standard = standardRepository.findById(studentStandardDTO.getStandard().getId());
        if (!standard.isPresent()) {
            throw new WitcurveException("No standard with given id");
        }
        Optional<Student> student = studentRepository.findById(studentStandardDTO.getStudent().getId());
        if (!student.isPresent()) {
            throw new WitcurveException("No student with given id");
        }
        if (!student.get().getSchoolInfo().equals(standard.get().getSchoolInfo())) {
            throw new WitcurveException("Student and the standard doesn't belong to same board");
        }
        Optional<AcademicSession> academicSession = academicSessionRepository.findById(studentStandardDTO.getSessionId());
        if (!academicSession.isPresent()) {
            throw new WitcurveException("No Academic Session with given id");
        }
        if (!academicSession.get().getActive()) {
            throw new WitcurveException("Academic Session is not active");
        }
        StudentStandard studentStandard = null;
        StudentStandard optionalStudentStandard;
        studentStandard = studentStandardRepository.getByStudentIdAndStandardIdAndSessionId(
            studentStandardDTO.getStudent().getId(), studentStandardDTO.getStandard().getId(), studentStandardDTO.getSessionId());

        if (studentStandard != null) {
            if (studentStandard.getActive()) {
                if (!studentStandardDTO.getRollNo().equals(studentStandard.getRollNo())) {
                    optionalStudentStandard = studentStandardRepository.getBySessionIdAndStandardIdAndRollNo(studentStandardDTO.getSessionId(), studentStandardDTO.getStandard().getId(), studentStandardDTO.getRollNo());
                    if (optionalStudentStandard != null) {
                        throw new WitcurveException("Roll No already exists");
                    }
                }
                studentStandardDTO.setId(studentStandard.getId());
                studentStandard = studentStandardRepository.save(studentStandardMapper.toEntity(studentStandardDTO));
            } else {
                optionalStudentStandard = studentStandardRepository.getBySessionIdAndStandardIdAndRollNo(studentStandardDTO.getSessionId(), studentStandardDTO.getStandard().getId(), studentStandardDTO.getRollNo());
                if (optionalStudentStandard != null) {
                    throw new WitcurveException("Roll No already exists");
                } else {
                    deactivateStudentStandard(Arrays.asList(studentStandardDTO.getStudent().getId()));
                    studentStandardDTO.setId(studentStandard.getId());
                    studentStandard = studentStandardRepository.save(studentStandardMapper.toEntity(studentStandardDTO));
                    createMandatoryCourseRecord(studentStandard);
                }
            }
        } else {
            optionalStudentStandard = studentStandardRepository.getBySessionIdAndStandardIdAndRollNo(studentStandardDTO.getSessionId(), studentStandardDTO.getStandard().getId(), studentStandardDTO.getRollNo());
            if (optionalStudentStandard != null) {
                throw new WitcurveException("Roll No already exists");
            } else {
                deactivateStudentStandard(Arrays.asList(studentStandardDTO.getStudent().getId()));
                studentStandard = studentStandardRepository.save(studentStandardMapper.toEntity(studentStandardDTO));
                createMandatoryCourseRecord(studentStandard);
            }
        }
        return studentStandardMapper.toDto(studentStandard);
    }

    @Override
    public StudentStandardDTO getByStudentId(Long studentId) throws WitcurveException {

        StudentStandard studentStandard;
        List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(studentId);
        if (studentStandards.isEmpty()) {
            return null;
        } else if (studentStandards.size() > 1) {
            throw new WitcurveException("There are more than one active student standard with given student id : " + studentId);
        } else {
            studentStandard = studentStandards.get(0);
        }
        return studentStandardMapper.toDto(studentStandard);

    }

    @Override
    public List<StudentStandardDTO> getByStandardIdAndCourseId(Long standardId, Long courseId) {
        List<StudentStandard> result;
        if(courseId == null) {
            result = studentStandardRepository.getByStandardId(standardId);
        } else {
            result = studentStandardRepository.getByStandardIdAndCourseId(standardId, courseId);
        }
        return studentStandardMapper.toDto(result);
    }

    @Override
    public List<StudentStandardDTO> getBySchoolInfoId(Long schoolInfoId, Grade grade, Long standardId) {
        if (standardId == null && grade == null) {
            return studentStandardMapper.toDto(studentStandardRepository.getBySchoolInfoId(schoolInfoId));
        } else if (standardId != null) {
            return studentStandardMapper.toDto(studentStandardRepository.getByStandardId(standardId));
        } else {
            return studentStandardMapper.toDto(studentStandardRepository.getBySchoolInfoIdAndGrade(schoolInfoId, grade));
        }

    }

    @Override
    public List<StudentStandardDTO> getByStaffId(Long staffId, Grade grade, Long standardId) throws WitcurveException {
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherId(staffId);
        if (courseTeachers.size() == 0) {
            throw new WitcurveException("This teacher is not attached to any courses or standards");
        }
        Set<Long> standardIds = new HashSet<>();
        if (standardId == null && grade == null) {
            standardIds = courseTeachers
                .stream()
                .map(CourseTeacher::getStandard)
                .map(s -> s.getId())
                .collect(Collectors.toSet());
        } else if (standardId != null) {
            standardIds.add(standardId);
        } else {
            for (CourseTeacher courseTeacher : courseTeachers) {
                if (courseTeacher.getStandard().getGrade().equals(grade)) {
                    standardIds.add(courseTeacher.getStandard().getId());
                }
            }
        }
        List<StudentStandard> studentStandards = studentStandardRepository.getByStandardsId(new ArrayList<>(standardIds));
        return studentStandardMapper.toDto(studentStandards);

    }

    public void deactivateStudentStandard(List<Long> studentIds) {
        studentStandardRepository.deactivateByStudentIds(studentIds);
        studentCourseRepository.deactivateStudentCourseByStudentIds(studentIds);
    }

    private void createMandatoryCourseRecord(StudentStandard studentStandard) {
        Optional<Standard> standardOptional = standardRepository.findById(studentStandard.getStandard().getId());
        if (!standardOptional.isPresent()) {
            throw new WitcurveException("There is not standard with given id :" + studentStandard.getStandard().getId());
        }
        List<Course> listOfMandatoryCourses = courseRepository.findMandatoryCourse(standardOptional.get().getSchoolInfo().getId(), standardOptional.get().getGrade());
        List<StudentCourseDTO> studentCourseDTOs = new ArrayList<>();
        if (!listOfMandatoryCourses.isEmpty()) {
            for (Course course : listOfMandatoryCourses) {
                StudentCourseDTO studentCourseDTO = new StudentCourseDTO();
                studentCourseDTO.setStudentStandardId(studentStandard.getId());
                studentCourseDTO.setCourseId(course.getId());
                studentCourseDTOs.add(studentCourseDTO);
            }
            studentCourseService.saveOrUpdate(studentCourseDTOs);
        }
    }
}
