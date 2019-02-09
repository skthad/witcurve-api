package com.witcurve.service.impl;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.StudentStandard;
import com.witcurve.repository.CourseTeacherRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

    @Override
    public List<StudentStandardDTO> save(List<StudentStandardDTO> studentStandardDTOs, Long standardId) {
        for (StudentStandardDTO studentStandardDTO : studentStandardDTOs) {
            studentStandardDTO.setId(null);
            studentStandardDTO.getStandard().setId(standardId);
            studentStandardDTO.setActive(Boolean.TRUE);
        }
        List<Long> studentIds = studentStandardDTOs.stream().map(s -> s.getStudent().getId()).collect(Collectors.toList());
        studentStandardRepository.deactivateByStudentIds(studentIds);

        List<StudentStandard> studentStandards = studentStandardRepository.saveAll(
            studentStandardMapper.toEntity(studentStandardDTOs));
        return studentStandardMapper.toDto(studentStandards);
    }

    @Override
    public StudentStandardDTO update(StudentStandardDTO studentStandardDTO) {
        studentStandardDTO.setId(null);
        studentStandardRepository.deactivateByStudentIds(Arrays.asList(studentStandardDTO.getStudent().getId()));
        StudentStandard studentStandard = studentStandardRepository.getByStudentIdAndStandardId(
            studentStandardDTO.getStudent().getId(), studentStandardDTO.getStandard().getId());
        if (studentStandard != null) {
            studentStandard.setActive(Boolean.TRUE);

        } else {
            studentStandard = studentStandardRepository.save(studentStandard);
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
    public List<StudentStandardDTO> getBySchoolInfoId(Long schoolInfoId) {
        return studentStandardMapper.toDto(studentStandardRepository.getBySchoolInfoId(schoolInfoId));
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
