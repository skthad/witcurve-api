package com.witcurve.service;

import com.witcurve.service.dto.SectionPerformanceDTO;

import java.util.List;

public interface AnalyticsService {
    List<SectionPerformanceDTO> getSectionPerformance(Long examId);
}
