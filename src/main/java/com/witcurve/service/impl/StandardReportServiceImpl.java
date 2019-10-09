package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.AttachmentType;
import com.witcurve.domain.enumeration.ReportStatus;
import com.witcurve.repository.ReportCardRepository;
import com.witcurve.repository.StandardReportRepository;
import com.witcurve.repository.StandardRepository;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.service.AttachmentService;
import com.witcurve.service.ReportCardService;
import com.witcurve.service.StandardReportService;
import com.witcurve.service.StudentReportService;
import com.witcurve.service.dto.StudentReportDTO;
import com.witcurve.service.mapper.StandardReportMapper;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@Transactional
public class StandardReportServiceImpl implements StandardReportService {

    private final Logger log = LoggerFactory.getLogger(StandardReportServiceImpl.class);

    @Autowired
    StandardReportMapper standardReportMapper;

    @Autowired
    StandardReportRepository standardReportRepository;

    @Autowired
    ReportCardService reportCardService;

    @Autowired
    StudentReportService studentReportService;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    ReportCardRepository reportCardRepository;

    public StandardReportDTO saveOrUpdate(StandardReportDTO standardReportDTO) {
        log.debug("Request to save standard report : {}", standardReportDTO);
        Long deleteWithHeaderAttachmentId = null, deleteWithoutHeaderAttachmentId = null;
        if(standardReportDTO.getStatus().equals(ReportStatus.FAILED)) {
            if(standardReportDTO.getFailureReason() == null) {
                throw new WitcurveException("Failed report status requires failure reason");
            }
        }
        if(standardReportDTO.getStatus().equals(ReportStatus.SUCCESS)) {
            if(standardReportDTO.getWithHeader() == null || standardReportDTO.getWithOutHeader()==null) {
                throw new WitcurveException("Success report status requires with and without header attachments");
            }
        }
        if(standardReportDTO.getId()!= null) {
            Optional<StandardReport> existingStandardReport = standardReportRepository.findById(standardReportDTO.getId());
            if(!existingStandardReport.isPresent()) {
                throw new WitcurveException("No standard report found with id : "+standardReportDTO.getId());
            }
            if(!standardReportDTO.getWithOutHeader().equals(existingStandardReport.get().getWithOutHeader())) {
                deleteWithoutHeaderAttachmentId = existingStandardReport.get().getWithOutHeader().getId();
            }
            if(!standardReportDTO.getWithHeader().equals(existingStandardReport.get().getWithHeader())) {
                deleteWithHeaderAttachmentId = existingStandardReport.get().getWithHeader().getId();
            }
        }
        StandardReport standardReport = standardReportMapper.toEntity(standardReportDTO);
        standardReport = standardReportRepository.save(standardReport);
        if(deleteWithHeaderAttachmentId != null) {
            attachmentService.delete(deleteWithHeaderAttachmentId);
        }
        if(deleteWithoutHeaderAttachmentId != null) {
            attachmentService.delete(deleteWithoutHeaderAttachmentId);
        }
        return standardReportMapper.toDto(standardReport);
    }

    public List<StandardReportDTO> findByExamId(Long examId) {
        log.debug("Request to get standard report for exam with id : {} ", examId);
        List<StandardReport> standardReports = standardReportRepository.findByExamId(examId);
        return standardReportMapper.toDto(standardReports);
    }

    public StandardReportDTO findById(Long id) {
        log.debug("Request to get standard report with id : {}", id);
        Optional<StandardReport> standardReport = standardReportRepository.findById(id);
        if(!standardReport.isPresent()) {
            throw new WitcurveException("No standard report found with id : "+id);
        }
        return standardReportMapper.toDto(standardReport.get());
    }

    public StandardReportDTO generateStandardReport(Long standardId, Long reportCardId) {
        log.debug("Request to generate standard report for standard with id : {} with report card id : {}", standardId, reportCardId);
        Optional<Standard> standard = standardRepository.findById(standardId);
        if (!standard.isPresent()) {
            throw new WitcurveException("No standard found with id : " + standardId);
        }
        Optional<ReportCard> reportCard = reportCardRepository.findById(reportCardId);
        if(!reportCard.isPresent()) {
            throw new WitcurveException("There is no report card setting available with id : "+reportCardId);
        }
        StandardReport standardReport = standardReportRepository.findByStandardIdAndReportCardId(standardId, reportCardId);
        StandardReportDTO standardReportDTO;
        if(standardReport == null) {
            standardReportDTO = new StandardReportDTO();
            standardReportDTO.setStandardId(standardId);
            standardReportDTO.setReportCardId(reportCardId);
            standardReportDTO.setStatus(ReportStatus.IN_PROCESS);
        } else {
            if(standardReport.getStatus().equals(ReportStatus.IN_PROCESS)) {
                throw new WitcurveException("Report card generation for this report is already in progress, so it cannot be generated again");
            }
            standardReport.setStatus(ReportStatus.IN_PROCESS);
            standardReportDTO = standardReportMapper.toDto(standardReport);
        }
        standardReportDTO = saveOrUpdate(standardReportDTO);
        return standardReportDTO;
    }

    @Async
    public void createReportCards(StandardReportDTO standardReportDTO) {
        log.debug("Request to create report cards with for standard report : {}", standardReportDTO);
        try {
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
            List<File> reportsWithHeader = new ArrayList<>(reportCardService.getReportCardPreviewForStandard(standardReportDTO.getReportCardId(), standardReportDTO.getStandardId(), pageable, true).getContent());
            List<File> reportsWithOutHeader = new ArrayList<>(reportCardService.getReportCardPreviewForStandard(standardReportDTO.getReportCardId(), standardReportDTO.getStandardId(), pageable, false).getContent());
            List<StudentStandard> studentStandards = new ArrayList<>(studentStandardRepository.getByStandardId(standardReportDTO.getStandardId(), pageable).getContent());
            File headerZipFile = WitcurveUtil.createTempFile(standardReportDTO.getStandardName()+"_report.zip");
            FileOutputStream fos = new FileOutputStream( headerZipFile.getPath());
            ZipOutputStream zos = new ZipOutputStream(fos);
            File noHeaderZipFile = WitcurveUtil.createTempFile(standardReportDTO.getStandardName()+"_report_without_header.zip");
            FileOutputStream fosNoHeader = new FileOutputStream( noHeaderZipFile.getPath());
            ZipOutputStream zosNoHeader = new ZipOutputStream(fosNoHeader);
            for(int count =0; count < studentStandards.size(); count++) {
                String destinationDirectory = AttachmentType.STUDENT_REPORT.toString()+File.separator+studentStandards.get(count).getStudent().getAdmissionId();
                Attachment studentAttachment = attachmentService.saveAttachmentWithFile(reportsWithHeader.get(count), AttachmentType.STUDENT_REPORT, destinationDirectory);
                List<StudentReportDTO> studentReports = studentReportService.getStudentReportByStudentId(studentStandards.get(count).getStudent().getId(), standardReportDTO.getReportCardId());
                if(studentReports.isEmpty()) {
                    StudentReportDTO studentReportDTO = new StudentReportDTO();
                    studentReportDTO.setStudentId(studentStandards.get(count).getStudent().getId());
                    studentReportDTO.setReportCardId(standardReportDTO.getReportCardId());
                    studentReportDTO.setAttachment(studentAttachment);
                    studentReportService.saveOrUpdateStudentReport(studentReportDTO);
                } else {
                    StudentReportDTO studentReportDTO = studentReports.get(0);
                    studentReportDTO.setAttachment(studentAttachment);
                    studentReportService.saveOrUpdateStudentReport(studentReportDTO);
                }

                //add header file to zip
                addToZipFile(reportsWithHeader.get(count), zos);

                //add no header file to zip
               addToZipFile(reportsWithOutHeader.get(count), zosNoHeader);
            }
            zos.close();
            zosNoHeader.close();
            fos.close();
            fosNoHeader.close();
            String destinationDirectory = AttachmentType.STANDARD_REPORT.toString()+File.separator+standardReportDTO.getExamName();
            Attachment attachmentWithHeader = attachmentService.saveAttachmentWithFile(headerZipFile, AttachmentType.STANDARD_REPORT, destinationDirectory);
            destinationDirectory = AttachmentType.STANDARD_REPORT_WITHOUT_HEADER.toString()+File.separator+standardReportDTO.getExamName();
            Attachment attachmentWithoutHeader = attachmentService.saveAttachmentWithFile(noHeaderZipFile, AttachmentType.STANDARD_REPORT_WITHOUT_HEADER, destinationDirectory);
            standardReportDTO.setStatus(ReportStatus.SUCCESS);
            standardReportDTO.setWithHeader(attachmentWithHeader);
            standardReportDTO.setWithOutHeader(attachmentWithoutHeader);
        } catch (FileNotFoundException e) {
            standardReportDTO.setStatus(ReportStatus.FAILED);
            standardReportDTO.setFailureReason("There was problem while creating zip files : "+e.getMessage());
        } catch (IOException e) {
            standardReportDTO.setStatus(ReportStatus.FAILED);
            standardReportDTO.setFailureReason("There was problem while files to zip files : "+e.getMessage());
        } catch (WitcurveException e) {
            standardReportDTO.setStatus(ReportStatus.FAILED);
            standardReportDTO.setFailureReason("Witcurve Exception : "+e.getMessage());
        } catch (Exception e) {
            standardReportDTO.setStatus(ReportStatus.FAILED);
            standardReportDTO.setFailureReason("UnknownException : "+e.getMessage());
        }
        saveOrUpdate(standardReportDTO);
    }

    private static void addToZipFile(File file, ZipOutputStream zos) throws IOException {
        System.out.println("Writing '" + file.getName() + "' to zip file");


        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }



}
