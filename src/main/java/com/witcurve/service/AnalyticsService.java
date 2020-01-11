package com.witcurve.service;

import com.witcurve.service.dto.SectionPerformanceDTO;
import com.witcurve.service.dto.StudentPerformanceDTO;
import com.witcurve.web.rest.vm.StudentPerformanceDashboardVM;

import java.util.List;

public interface AnalyticsService {
    List<SectionPerformanceDTO> getSectionPerformance(Long examId);

    StudentPerformanceDTO getStudentPerformanceByStudentId(Long studentId);

    List<StudentPerformanceDashboardVM> getStudentDashboardDetailsBySchoolInfoId(Long schoolInfoId);

}
