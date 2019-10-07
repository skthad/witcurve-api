package com.witcurve.service.impl;

import com.witcurve.domain.StudentReport;
import com.witcurve.repository.StudentReportRepository;
import com.witcurve.service.AttachmentService;
import com.witcurve.service.StudentReportService;
import com.witcurve.service.dto.StudentReportDTO;
import com.witcurve.service.mapper.StudentReportMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentReportServiceImpl implements StudentReportService {

    private final Logger log = LoggerFactory.getLogger(StudentReportServiceImpl.class);

    @Autowired
    StudentReportRepository studentReportRepository;

    @Autowired
    StudentReportMapper studentReportMapper;

    @Autowired
    AttachmentService attachmentService;

    @Override
    public StudentReportDTO saveOrUpdateStudentReport(StudentReportDTO studentReportDTO) {
        log.debug("Request to save or update studentReport: {}", studentReportDTO);
        StudentReport studentReport = studentReportMapper.toEntity(studentReportDTO);
        studentReport = studentReportRepository.save(studentReport);
        return studentReportMapper.toDto(studentReport);
    }

    @Override
    public StudentReportDTO getStudentReportById(Long studentReportId) {
        log.debug("Request to get studentReport with id : {}", studentReportId);
        Optional<StudentReport> studentReport = studentReportRepository.findById(studentReportId);
        if (!studentReport.isPresent()) {
            throw new WitcurveException("No StudentReport with given id " + studentReportId);
        }
        return studentReportMapper.toDto(studentReport.get());
    }

    @Override
    public List<StudentReportDTO> getStudentReportByStudentId(Long studentId) {
        log.debug("Request to get studentReport with student id : {}", studentId);
        List<StudentReport> studentReports = studentReportRepository.findByStudentId(studentId);
        if (studentReports.isEmpty()) {
            throw new WitcurveException("No StudentReport with given studentId " + studentId);
        }
        return studentReportMapper.toDto(studentReports);
    }

    @Override
    public void deleteStudentReportById(Long studentReportId) {
        log.debug("Request to delete studentReport with id {}", studentReportId);
        Optional<StudentReport> studentReport = studentReportRepository.findById(studentReportId);
        if (!studentReport.isPresent()) {
            throw new WitcurveException("No StudentReport with given id " + studentReportId);
        }
        studentReportRepository.deleteById(studentReportId);
        attachmentService.delete(studentReport.get().getAttachment().getId());
    }
}




