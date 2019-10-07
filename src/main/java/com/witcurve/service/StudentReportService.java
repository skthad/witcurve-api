package com.witcurve.service;

import com.witcurve.service.dto.StudentReportDTO;

import java.util.List;

public interface StudentReportService {

    StudentReportDTO saveOrUpdateStudentReport(StudentReportDTO studentReportDTO);

    StudentReportDTO getStudentReportById(Long studentReportId);

    List<StudentReportDTO> getStudentReportByStudentId(Long studentId, Long reportCardId);

    void deleteStudentReportById(Long studentReportId);
}
