package com.witcurve.service.impl;

import com.witcurve.domain.Event;
import com.witcurve.domain.Exam;
import com.witcurve.domain.ReportCardDesign;
import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.repository.*;
import com.witcurve.service.ReportCardDesignService;
import com.witcurve.service.dto.ReportCardDesignDTO;
import com.witcurve.service.mapper.ReportCardDesignMapper;
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
public class ReportCardDesignServiceImpl implements ReportCardDesignService {

    private final Logger log = LoggerFactory.getLogger(ReportCardDesignServiceImpl.class);

    @Autowired
    ReportCardDesignMapper reportCardDesignMapper;

    @Autowired
    ReportCardDesignRepository reportCardDesignRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    StudentMarksRepository studentMarksRepository;

    @Autowired
    ExamRepository examRepository;

    @Override
    public List<ReportCardDesignDTO> saveOrUpdate(List<ReportCardDesignDTO> reportCardDesignDTOS, Long examId, String bindingId) {
        log.debug("Request to save ReportCardDesigns : {} for exam with id : {} or periodic test with bindingId : {}", reportCardDesignDTOS, examId, bindingId);
        validAndFormatReportCardDesigns(reportCardDesignDTOS, examId, bindingId);
        List<ReportCardDesign> reportCardDesigns = reportCardDesignMapper.toEntity(reportCardDesignDTOS);
        reportCardDesigns = reportCardDesignRepository.saveAll(reportCardDesigns);
        validTotalReportCardRecords(examId, bindingId);
        return reportCardDesignMapper.toDto(reportCardDesigns);
    }

    @Override
    public List<ReportCardDesignDTO> findByExamIdOrBindingIdWithFieldType(Long examId, String bindingId, ReportFieldType fieldType) {
        log.debug("Request to get reportCardDesigns of field type : {} for exam with id : {} or periodic test with binding id : {}", fieldType, examId, bindingId);
        List<ReportCardDesign> result;
        anyOne(examId, bindingId);
        if(examId != null) {
            if(fieldType == null) {
                result = reportCardDesignRepository.findByExam(examId);
            } else {
                result = reportCardDesignRepository.findByFieldTypeAndExam(fieldType, examId);
            }
        } else {
            if(fieldType == null) {
                result = reportCardDesignRepository.findByBindingId(bindingId);
            } else {
                result = reportCardDesignRepository.findByFieldTypeAndBindingId(fieldType, bindingId);
            }
        }
        return reportCardDesignMapper.toDto(result);
    }

    @Override
    public ReportCardDesignDTO findById(Long id) {
        log.debug("Request to get reportCardDesign by id : {}", id);
        Optional<ReportCardDesign> reportCardDesign = reportCardDesignRepository.findById(id);
        if(!reportCardDesign.isPresent()) {
            throw new WitcurveException("No Report Card Design is found wit id : "+id);
        }
        return reportCardDesignMapper.toDto(reportCardDesign.get());
    }

    @Override
    public void deleteReportCardDesign(List<Long> ids) {
        log.debug("Request to delete report card design with ids : {}");
        List<ReportCardDesign> reportCardDesigns = reportCardDesignRepository.findAllById(ids);
        for(ReportCardDesign reportCardDesign : reportCardDesigns) {
            if(!reportCardDesign.getFieldType().equals(ReportFieldType.MANUAL_ENTRY)) {
                throw new WitcurveException("You can only delete manual entry report card designs");
            }
        }
        studentMarksRepository.deleteStudentMarksByRcdIds(ids);
        reportCardDesignRepository.deleteByIds(ids);
    }

    private void validAndFormatReportCardDesigns(List<ReportCardDesignDTO> reportCardDesignDTOS, Long examId, String bindingId) {
        anyOne(examId, bindingId);
        Boolean mainRecordExists = false, totalRecordExists = false,
            remarksRecordExists =false, attendanceRecordExists = false;
        Boolean forExam = false;
        List<ReportCardDesign> existingReportCardDesigns = null;
        if(examId != null) {
            Optional<Exam> exam = examRepository.findById(examId);
            if(!exam.isPresent()) {
                throw new WitcurveException("No Exam with given Id " + examId);
            }
            if(exam.get().getStatus().equals(ExamStatus.DRAFT)) {
                throw new WitcurveException("Draft exams cannot have report card design");
            }
            forExam = true;
        } else {
            List<Event> events = eventRepository.findPeriodicEventsByBindingId(bindingId);
            if(events.size() == 0) {
                throw new WitcurveException("No Periodic Test exists with given bindingId " + bindingId);
            }
        }
        for(ReportCardDesignDTO reportCardDesignDTO : reportCardDesignDTOS) {
            if(forExam) {
                reportCardDesignDTO.setExamId(examId);
                reportCardDesignDTO.setBindingId(null);
            } else {
                reportCardDesignDTO.setBindingId(bindingId);
                reportCardDesignDTO.setExamId(null);
            }
            ReportFieldType fieldType = reportCardDesignDTO.getFieldType();
            switch (reportCardDesignDTO.getFieldType()) {
                case MAIN:
                case TOTAL:
                    if(reportCardDesignDTO.getShowMarksOnly() == null && reportCardDesignDTO.getShowGradesOnly() == null) {
                        throw new WitcurveException("There should be at least one true among show marks only and show grades only");
                    }
                    if(!reportCardDesignDTO.getShowGradesOnly() && !reportCardDesignDTO.getShowMarksOnly()) {
                        throw new WitcurveException("There should be at least one true among show marks only and show grades only");
                    }
                    if(fieldType.equals(ReportFieldType.REMARKS)) {
                        if(mainRecordExists) {
                            throw new WitcurveException("There should be only one main field type record for a model");
                        }
                    } else {
                        if(totalRecordExists) {
                            throw new WitcurveException("There should be only one total field type record for a model");
                        }
                    }
                    if(forExam) {
                        existingReportCardDesigns = reportCardDesignRepository.findByFieldTypeAndExam(fieldType, examId);
                    } else {
                        existingReportCardDesigns = reportCardDesignRepository.findByFieldTypeAndBindingId(fieldType, bindingId);
                    }

                    if(existingReportCardDesigns.size()==0) {
                        if(reportCardDesignDTO.getId() != null) {
                            throw new WitcurveException("There doesn't exists a report card design of "+fieldType.toString().toLowerCase()+" field type to update with given id : "+reportCardDesignDTO.getId());
                        }
                    } else if(existingReportCardDesigns.size() == 1) {
                        if(reportCardDesignDTO.getId() == null) {
                            throw new WitcurveException("There already exists a report card design "+fieldType.toString().toLowerCase()+" field type for this model, so another record cannot be created");
                        } else {
                            if(reportCardDesignDTO.getId() != existingReportCardDesigns.get(0).getId()) {
                                throw new WitcurveException("Id of the "+fieldType.toString().toLowerCase()+" field type with id "+reportCardDesignDTO.getId()+" doesn't match with existing record");
                            }
                        }
                    } else {
                        throw new WitcurveException("There are multiple main type records stored for this model");
                    }
                    if(reportCardDesignDTO.getMarks() == null || reportCardDesignDTO.getShowGradesOnly() == null || reportCardDesignDTO.getShowMarksOnly() == null) {
                        throw new WitcurveException("Main field type record needs to have marks, show grades and show marks option values");
                    }
                    if(fieldType.equals(ReportFieldType.MAIN)) {
                        if(forExam) {
                            reportCardDesignDTO.setName("Exam");
                            reportCardDesignDTO.setShortForm("Exam");
                        } else {
                            reportCardDesignDTO.setName("Periodic Test");
                            reportCardDesignDTO.setShortForm("P.T.");
                        }
                        mainRecordExists = true;
                    } else {
                        reportCardDesignDTO.setName("Total");
                        reportCardDesignDTO.setShortForm("Total");
                        totalRecordExists = true;
                    }
                    reportCardDesignDTO.setSelected(false);
                    break;
                case REMARKS:
                case ATTENDANCE:
                    if(fieldType.equals(ReportFieldType.REMARKS)) {
                        if(remarksRecordExists) {
                            throw new WitcurveException("There should be only one remarks field type record for this model");
                        }
                    } else {
                        if(attendanceRecordExists) {
                            throw new WitcurveException("There should be only one attendance field type record for this model");
                        }
                    }
                    if(forExam) {
                        existingReportCardDesigns = reportCardDesignRepository.findByFieldTypeAndExam(fieldType, examId);
                    } else {
                        existingReportCardDesigns = reportCardDesignRepository.findByFieldTypeAndBindingId(fieldType, bindingId);
                    }
                    if(existingReportCardDesigns.size()==0) {
                        if(reportCardDesignDTO.getId() != null) {
                            throw new WitcurveException("There doesn't exists a report card design of "+fieldType.toString().toLowerCase()+" field type to update with id : "+reportCardDesignDTO.getId());
                        }
                    } else if(existingReportCardDesigns.size() == 1) {
                        if(reportCardDesignDTO.getId() == null) {
                            throw new WitcurveException("There already exists a report card design "+fieldType.toString().toLowerCase()+" field type for this model, so another record cannot be created");
                        } else {
                            if(reportCardDesignDTO.getId() != existingReportCardDesigns.get(0).getId()) {
                                throw new WitcurveException("Id of the remarks field type : "+reportCardDesignDTO.getId()+" doesn't match with existing record");
                            }
                        }
                    } else {
                        throw new WitcurveException("There are multiple "+fieldType.toString().toLowerCase()+" type records stored for this model");
                    }
                    if(reportCardDesignDTO.getSelected() == null) {
                        throw new WitcurveException(fieldType.toString().toLowerCase()+" field type record needs to have selected field value");
                    }
                    reportCardDesignDTO.setShortForm(null);
                    reportCardDesignDTO.setMarks(null);
                    reportCardDesignDTO.setShowGradesOnly(null);
                    reportCardDesignDTO.setShowMarksOnly(null);
                    if(fieldType.equals(ReportFieldType.REMARKS)) {
                        remarksRecordExists = true;
                    } else {
                        attendanceRecordExists = true;
                    }
                    break;
                case MANUAL_ENTRY:
                    if(reportCardDesignDTO.getShowMarksOnly() == null && reportCardDesignDTO.getShowGradesOnly() == null) {
                        throw new WitcurveException("There should be at least one true among show marks only and show grades only");
                    }
                    if(!reportCardDesignDTO.getShowGradesOnly() && !reportCardDesignDTO.getShowMarksOnly()) {
                        throw new WitcurveException("There should be at least one true among show marks only and show grades only");
                    }
                    if(reportCardDesignDTO.getId() != null) {
                        Optional<ReportCardDesign> existingRecord = reportCardDesignRepository.findById(reportCardDesignDTO.getId());
                        if(existingRecord.isPresent()) {
                            if(!existingRecord.get().getFieldType().equals(ReportFieldType.MANUAL_ENTRY)) {
                                throw new WitcurveException("Field type cannot be changed");
                            }
                        } else {
                            throw new WitcurveException("There is no record available with id : "+reportCardDesignDTO.getId());
                        }
                    }
                    if(reportCardDesignDTO.getName() == null || reportCardDesignDTO.getShortForm() == null
                        || reportCardDesignDTO.getMarks() == null || reportCardDesignDTO.getShowGradesOnly() == null
                        || reportCardDesignDTO.getOrder()== null) {
                        throw new WitcurveException("Manual Entry field type record needs to have name, shortForm, marks, order and show grade only values");
                    }
                    reportCardDesignDTO.setSelected(null);
                    break;
            }
        }

    }

    private void anyOne(Long examId, String bindingId) {
        if(examId == null && bindingId == null) {
            throw new WitcurveException("Both examId and bindingId cannot be null");
        }
        if(examId != null && bindingId != null) {
            throw new WitcurveException("Both examId and bindingId cannot be not null");
        }
    }

    private void validTotalReportCardRecords(Long examId, String bindingId) {
        Double totalCalculated = 0.00, totalRecordMarks =null;
        List<ReportCardDesign> reportCardDesigns;
        if(examId != null) {
             reportCardDesigns = reportCardDesignRepository.findByExam(examId);
        } else {
            reportCardDesigns = reportCardDesignRepository.findByBindingId(bindingId);
        }
        for(ReportCardDesign reportCardDesign : reportCardDesigns) {
            if(reportCardDesign.getFieldType().equals(ReportFieldType.MAIN) ||
            reportCardDesign.getFieldType().equals(ReportFieldType.MANUAL_ENTRY)) {
                totalCalculated += reportCardDesign.getMarks();
            } else if(reportCardDesign.getFieldType().equals(ReportFieldType.TOTAL)) {
                totalRecordMarks = reportCardDesign.getMarks();
            }
        }
        if(totalRecordMarks != null) {
            if(!totalCalculated.equals(totalRecordMarks)) {
                throw new WitcurveException("Total marks don't add up, please check them");
            }
        }
    }

}
