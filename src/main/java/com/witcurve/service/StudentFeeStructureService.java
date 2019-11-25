package com.witcurve.service;

import com.witcurve.service.dto.StudentFeeStructureDTO;

import java.util.List;

public interface StudentFeeStructureService {

    StudentFeeStructureDTO saveOrUpdate(StudentFeeStructureDTO studentFeeStructureDTO);

    StudentFeeStructureDTO getByStudentIdAndSessionId(Long studentId, Long sessionId);

    List<StudentFeeStructureDTO> getByStandardIdAndSessionId(Long standardId, Long sessionId);
}
