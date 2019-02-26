package com.witcurve.service.impl;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.Standard;
import com.witcurve.domain.Student;
import com.witcurve.domain.StudentStandard;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.repository.StandardRepository;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.service.StudentStandardService;
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

    private final Logger log  = LoggerFactory.getLogger(StudentStandardServiceImpl.class);

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

    @Override
    public List<StudentStandardDTO> saveMultiple(List<StudentStandardDTO> studentStandardDTOs, Long standardId) throws WitcurveException {
        List<String> rollNos = new ArrayList<>();
        //TODO to check if all student ids belong to standard school info id
        for (StudentStandardDTO studentStandardDTO : studentStandardDTOs) {
            studentStandardDTO.setId(null);
            studentStandardDTO.getStandard().setId(standardId);
            studentStandardDTO.setActive(Boolean.TRUE);
            rollNos.add(studentStandardDTO.getRollNo());
        }
        List<StudentStandard> existingRollNos = studentStandardRepository.getByStandardIdAndRollNos(standardId, rollNos);
        if(existingRollNos.size() !=0) {
            throw new WitcurveException("There already exists a student in given standard with entered roll no.");
        }
        List<Long> studentIds = studentStandardDTOs.stream().map(s -> s.getStudent().getId()).collect(Collectors.toList());
        studentStandardRepository.deactivateByStudentIds(studentIds);
        List<StudentStandard> studentStandards = studentStandardRepository.saveAll(
            studentStandardMapper.toEntity(studentStandardDTOs));
        return studentStandardMapper.toDto(studentStandards);
    }

    @Override
    public StudentStandardDTO save(StudentStandardDTO studentStandardDTO) throws WitcurveException {
        studentStandardDTO.setId(null);
        Optional<Standard> standard = standardRepository.findById(studentStandardDTO.getStandard().getId());
        if(!standard.isPresent()) {
            throw new WitcurveException("No standard with given id");
        }
        Optional<Student> student = studentRepository.findById(studentStandardDTO.getStudent().getId());
        if (!student.isPresent()) {
            throw new WitcurveException("No student with given id");
        }
        if(!student.get().getSchoolInfo().equals(standard.get().getSchoolInfo())) {
            throw new WitcurveException("Student and the standard doesn't belong to same board");
        }
        studentStandardRepository.deactivateByStudentIds(Arrays.asList(studentStandardDTO.getStudent().getId()));
        StudentStandard studentStandard = studentStandardRepository.getByStudentIdAndStandardId(
            studentStandardDTO.getStudent().getId(), studentStandardDTO.getStandard().getId());
        if (studentStandard != null) {
            List<StudentStandard> existingRollNos = studentStandardRepository.getByStandardIdAndRollNo(studentStandardDTO.getStandard().getId(), studentStandardDTO.getRollNo());
            if(existingRollNos.size() ==1 && !existingRollNos.get(0).equals(studentStandard)) {
                throw new WitcurveException("There already exists a student in given standard with entered roll no.");
            }
            studentStandard.setActive(Boolean.TRUE);
            studentStandard.setRollNo(studentStandardDTO.getRollNo());
        } else {
            studentStandardDTO.setId(null);
            List<StudentStandard> existingRollNos = studentStandardRepository.getByStandardIdAndRollNo(studentStandardDTO.getStandard().getId(), studentStandardDTO.getRollNo());
            if(existingRollNos.size()!=0) {
                throw new WitcurveException("There already exists a student in given standard with entered roll no.");
            }
            studentStandard = studentStandardRepository.save(studentStandardMapper.toEntity(studentStandardDTO));
        }
        return studentStandardMapper.toDto(studentStandard);
    }

    @Override
    public StudentStandardDTO getByStudentId(Long studentId) throws WitcurveException {

        StudentStandard studentStandard;
        List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(studentId);
        if(studentStandards.isEmpty()) {
            return null;
        } else if (studentStandards.size() > 1) {
            throw new WitcurveException("There are more than one active student standard with given student id : "+studentId);
        } else {
            studentStandard = studentStandards.get(0);
        }
        return studentStandardMapper.toDto(studentStandard);

    }

    @Override
    public List<StudentStandardDTO> getByStandardId(Long standardId) {
        return studentStandardMapper.toDto(studentStandardRepository.getByStandardId(standardId));
    }

    @Override
    public List<StudentStandardDTO> getBySchoolInfoId(Long schoolInfoId, Grade grade, Long standardId) {
        if(standardId == null && grade == null) {
            return studentStandardMapper.toDto(studentStandardRepository.getBySchoolInfoId(schoolInfoId));
        } else if(standardId != null) {
            return studentStandardMapper.toDto(studentStandardRepository.getByStandardId(standardId));
        } else {
            return studentStandardMapper.toDto(studentStandardRepository.getBySchoolInfoIdAndGrade(schoolInfoId, grade));
        }

    }

    @Override
    public List<StudentStandardDTO> getByStaffId(Long staffId) throws WitcurveException {
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherId(staffId);
        if(courseTeachers.size() ==0) {
            throw new WitcurveException("This teacher is not attached to any courses or standards");
        }
        Set<Long> standardIds = courseTeachers
            .stream()
            .map(CourseTeacher::getStandard)
            .map(s -> s.getId())
            .collect(Collectors.toSet());

        List<StudentStandard> studentStandards = studentStandardRepository.getByStandardsId(new ArrayList<>(standardIds));
        return studentStandardMapper.toDto(studentStandards);

    }


}
