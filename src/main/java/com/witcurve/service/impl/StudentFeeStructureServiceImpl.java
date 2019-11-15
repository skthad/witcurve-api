package com.witcurve.service.impl;

import com.witcurve.domain.StudentFeeStructure;
import com.witcurve.repository.StudentFeeStructureRepository;
import com.witcurve.service.StudentFeeStructureService;
import com.witcurve.service.dto.StudentFeeStructureDTO;
import com.witcurve.service.mapper.StudentFeeStructureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentFeeStructureServiceImpl implements StudentFeeStructureService {

    private final Logger log = LoggerFactory.getLogger(StudentFeeStructureServiceImpl.class);

    @Autowired
    StudentFeeStructureRepository studentfeeStructureRepository;

    @Autowired
    StudentFeeStructureMapper studentFeeStructureMapper;

    @Autowired
    public StudentFeeStructureDTO saveOrUpdate(StudentFeeStructureDTO studentFeeStructureDTO) {
        log.debug("Request to save or update StudentFeeStructure : {} ", studentFeeStructureDTO);
        StudentFeeStructure studentFeeStructure = studentfeeStructureRepository.save(studentFeeStructureMapper.toEntity(studentFeeStructureDTO));
        return studentFeeStructureMapper.toDto(studentFeeStructure);
    }
}
