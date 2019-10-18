package com.witcurve.service;

import com.witcurve.service.dto.SectionPerformanceDTO;
import com.witcurve.service.dto.StudentPerformanceDTO;

import java.util.List;

public interface AnalyticsService {
    List<SectionPerformanceDTO> getSectionPerformance(Long examId);

    StudentPerformanceDTO getStudentPerformanceByStudentId(Long studentId);

}
