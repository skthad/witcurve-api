package com.witcurve.service.impl;

import com.witcurve.domain.StudentStandard;
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

import java.util.List;

@Service
@Transactional
public class StudentStandardServiceImpl implements StudentStandardService {

    private final Logger log  = LoggerFactory.getLogger(StudentStandardServiceImpl.class);

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    StudentStandardMapper studentStandardMapper;

    @Override
    public StudentStandardDTO getByStudentId(Long studentId) throws WitcurveException {

        StudentStandard studentStandard = null;
        List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(studentId);
        if(studentStandards.isEmpty()) {
            throw new WitcurveException("There is no student standard with given student id : "+studentId);
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
}
