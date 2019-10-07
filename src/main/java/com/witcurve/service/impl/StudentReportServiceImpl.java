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

import java.util.ArrayList;
import java.util.Arrays;
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
        Long deleteAttachmentId = null;
        if(studentReportDTO .getId() != null) {
            Optional<StudentReport> existingStudentReport = studentReportRepository.findById(studentReportDTO.getId());
            if(!existingStudentReport.isPresent()) {
                throw new WitcurveException("No existing report card found with id : "+ studentReportDTO.getId());
            }
            if(!studentReportDTO.getAttachment().equals(existingStudentReport.get().getAttachment())) {
                deleteAttachmentId = existingStudentReport.get().getAttachment().getId();
            }
        }
        StudentReport studentReport = studentReportMapper.toEntity(studentReportDTO);
        studentReport = studentReportRepository.save(studentReport);
        if(deleteAttachmentId != null) {
            attachmentService.delete(deleteAttachmentId);
        }
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
    public List<StudentReportDTO> getStudentReportByStudentId(Long studentId, Long reportCardId) {
        log.debug("Request to get studentReport for student with id : {} and for report card with id : {}", studentId, reportCardId);
        if(reportCardId == null) {
            return studentReportMapper.toDto(studentReportRepository.findByStudentId(studentId));
        } else {
            StudentReport studentReport = studentReportRepository.findByStudentIdAndReportCardId(studentId, reportCardId);
            if(studentReport == null) {
                return new ArrayList<>();
            } else {
                return Arrays.asList(studentReportMapper.toDto(studentReport));
            }

        }
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




