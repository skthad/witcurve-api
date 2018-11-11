package com.witcurve.service.impl;

import com.witcurve.domain.StudentMarks;
import com.witcurve.repository.StudentMarksRepository;
import com.witcurve.service.AssessmentService;
import com.witcurve.service.dto.StudentMarksDTO;
import com.witcurve.service.mapper.StudentMarksMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

    private final Logger log  = LoggerFactory.getLogger(AssessmentServiceImpl.class);

    @Autowired
    StudentMarksRepository studentMarksRepository;

    @Autowired
    StudentMarksMapper studentMarksMapper;

    @Override
    public List<StudentMarksDTO> enterStudentMarks(List<StudentMarksDTO> studentMarks) {
        List<StudentMarks> savedStudentMarks = studentMarksRepository.saveAll(studentMarksMapper.toEntity(studentMarks));
        return studentMarksMapper.toDto(savedStudentMarks);
    }

    @Override
    public List<StudentMarksDTO> getStudentMarksByTestId(Long testId) {
        return studentMarksMapper.toDto(studentMarksRepository.getByTestId(testId));
    }

    @Override
    public List<StudentMarksDTO> getStudentMarksByStudentAndTestId(Long studentId, Long testId) {
        return studentMarksMapper.toDto(studentMarksRepository.getByStudentAndTestId(studentId, testId));
    }
}
