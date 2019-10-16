package com.witcurve.service;

import com.witcurve.service.dto.StudentPerformanceDTO;

public interface StudentPerformanceService {

    StudentPerformanceDTO getStudentPerformanceByStudentId(Long studentId);
}
