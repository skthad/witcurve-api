package com.witcurve.service.impl;

import com.witcurve.domain.Exam;
import com.witcurve.domain.ReportCardDesign;
import com.witcurve.domain.enumeration.CalculationType;
import com.witcurve.domain.enumeration.Grade;
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
    public List<ReportCardDesignDTO> saveOrUpdate(List<ReportCardDesignDTO> reportCardDesignDTOS, Long examId, Grade grade) {
        log.debug("Request to save ReportCardDesigns : {} for exam with id : {} for grade : {}", reportCardDesignDTOS, examId, grade);
        validAndFormatReportCardDesigns(reportCardDesignDTOS, examId, grade);
        List<ReportCardDesign> reportCardDesigns = reportCardDesignMapper.toEntity(reportCardDesignDTOS);
//        for(ReportCardDesign reportCardDesign : reportCardDesigns) {
////            if(reportCardDesign.getFieldType().equals(ReportFieldType.ATTRIBUTES) {
////
////            }
////        }
        reportCardDesigns = reportCardDesignRepository.saveAll(reportCardDesigns);
        validTotalReportCardRecords(examId, grade);
        return reportCardDesignMapper.toDto(reportCardDesigns);
    }

    @Override
    public List<ReportCardDesignDTO> findByExamIdOrBindingIdWithFieldType(Grade grade, Long examId, ReportFieldType fieldType) {
        log.debug("Request to get reportCardDesigns of field type : {} for exam with id : {} for grade : {}", fieldType, examId, grade);
        List<ReportCardDesign> result;
        if(fieldType == null) {
            result = reportCardDesignRepository.findByExamAndGrade(examId,grade);
        } else {
            result = reportCardDesignRepository.findByFieldTypeAndExamAndGrade(fieldType, examId, grade);
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
//        for(ReportCardDesign reportCardDesign : reportCardDesigns) {
//            if(!reportCardDesign.getFieldType().equals(ReportFieldType.MANUAL_ENTRY)) {
//                throw new WitcurveException("You can only delete manual entry report card designs");
//            }
//        }
        studentMarksRepository.deleteStudentMarksByRcdIds(ids);
        reportCardDesignRepository.deleteByIds(ids);
    }

    private void validAndFormatReportCardDesigns(List<ReportCardDesignDTO> reportCardDesignDTOS, Long examId, Grade grade) {
        Boolean mainRecordExists = false, totalRecordExists = false,
            remarksRecordExists =false, attendanceRecordExists = false,
            periodicTestRecordExists=false, nonScholasticRecordExists=false,
            attributeRecordExists = false;
        List<ReportCardDesign> existingReportCardDesigns = null;
        Optional<Exam> exam = examRepository.findById(examId);
        if(!exam.isPresent()) {
            throw new WitcurveException("No Exam with given Id " + examId);
        }
        for(ReportCardDesignDTO reportCardDesignDTO : reportCardDesignDTOS) {
            reportCardDesignDTO.setGrade(grade);
            reportCardDesignDTO.setExamId(examId);
            ReportFieldType fieldType = reportCardDesignDTO.getFieldType();
            switch (reportCardDesignDTO.getFieldType()) {
                case MAIN:
                case TOTAL:
                case PERIODIC_TEST:
                case NON_SCHOLASTIC:
                case ATTRIBUTES:
                    if(fieldType.equals(ReportFieldType.MAIN)) {
                        if(mainRecordExists) {
                            throw new WitcurveException("There should be only one main field type record for a model");
                        }
                    } else if(fieldType.equals(ReportFieldType.TOTAL)){
                        if(totalRecordExists) {
                            throw new WitcurveException("There should be only one total field type record for a model");
                        }
                    } else if(fieldType.equals(ReportFieldType.PERIODIC_TEST)){
                        if(periodicTestRecordExists) {
                            throw new WitcurveException("There should be only one periodic test field type record for a model");
                        }
                    } else if(fieldType.equals(ReportFieldType.ATTRIBUTES)){
                        if(attributeRecordExists) {
                            throw new WitcurveException("There should be only one attribute field type record for a model");
                        }
                    } else {
                        if(nonScholasticRecordExists) {
                            throw new WitcurveException("There should be only one non scholastic field type record for a model");
                        }
                    }
                    existingReportCardDesigns = reportCardDesignRepository.findByFieldTypeAndExamAndGrade(fieldType, examId, grade);

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
                        throw new WitcurveException("There are multiple "+fieldType+" type records stored for this model");
                    }
                    if(!reportCardDesignDTO.getFieldType().equals(ReportFieldType.ATTRIBUTES)) {
                        if(reportCardDesignDTO.getMarks() == null) {
                            throw new WitcurveException(fieldType+" type record needs to have marks");
                        }
                    }
                    if(fieldType.equals(ReportFieldType.MAIN)) {
                        reportCardDesignDTO.setName("Exam");
                        reportCardDesignDTO.setShortForm("Exam");
                        mainRecordExists = true;
                    } else if(fieldType.equals(ReportFieldType.TOTAL)) {
                        reportCardDesignDTO.setName("Total");
                        reportCardDesignDTO.setShortForm("Total");
                        totalRecordExists = true;
                    } else if(fieldType.equals(ReportFieldType.NON_SCHOLASTIC)) {
                        if(reportCardDesignDTO.getCourseDTOs() == null || reportCardDesignDTO.getCourseDTOs().size() == 0) {
                            throw new WitcurveException("Non scholastic field requires list of courses");
                        }
                        nonScholasticRecordExists = true;
                    } else if(fieldType.equals(ReportFieldType.ATTRIBUTES)) {
                        if(reportCardDesignDTO.getAttributeDTOs() == null || reportCardDesignDTO.getAttributeDTOs().size() == 0) {
                            throw new WitcurveException("Attribute field requires list of attributes");
                        }
                        attributeRecordExists = true;
                    } else {
                        if(reportCardDesignDTO.getSelectedPeriodicTests() == null || reportCardDesignDTO.getSelectedPeriodicTests().size() == 0) {
                            throw new WitcurveException("Periodic Test field requires list of binding id values");
                        }
                        if(reportCardDesignDTO.getCalculationType() == null) {
                            throw new WitcurveException("Periodic Test field requires list of calculation type field");
                        } else {
                            if(reportCardDesignDTO.getCalculationType().equals(CalculationType.BEST_OF)) {
                                if(reportCardDesignDTO.getBestOfValue() == null) {
                                    throw new WitcurveException("Needs best of value");
                                } else {
                                    if(reportCardDesignDTO.getBestOfValue() > reportCardDesignDTO.getSelectedPeriodicTests().size()) {
                                        throw new WitcurveException("Best of value is more than periodic tests selected");
                                    }
                                }
                            }
                        }
                        reportCardDesignDTO.setName("Periodic Test");
                        reportCardDesignDTO.setShortForm("P.T.");
                        periodicTestRecordExists = true;
                    }
                    break;
                case REMARKS:
                    if(fieldType.equals(ReportFieldType.REMARKS)) {
                        if(remarksRecordExists) {
                            throw new WitcurveException("There should be only one remarks field type record for this model");
                        }
                    } else {
                        if(attendanceRecordExists) {
                            throw new WitcurveException("There should be only one attendance field type record for this model");
                        }
                    }
                    existingReportCardDesigns = reportCardDesignRepository.findByFieldTypeAndExamAndGrade(fieldType, examId, grade);
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
                    if(fieldType.equals(ReportFieldType.REMARKS)) {
                        remarksRecordExists = true;
                    } else {
                        attendanceRecordExists = true;
                    }
                    break;
                case MANUAL_ENTRY:
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
                        || reportCardDesignDTO.getMarks() == null
                        || reportCardDesignDTO.getOrder()== null) {
                        throw new WitcurveException("Manual Entry field type record needs to have name, shortForm, marks, order and show grade only values");
                    }
                    reportCardDesignDTO.setSelected(null);
                    break;


            }
        }

    }

    private void validTotalReportCardRecords(Long examId, Grade grade) {
        Double totalCalculated = 0.00, totalRecordMarks =null;
        List<ReportCardDesign> reportCardDesigns;
        reportCardDesigns = reportCardDesignRepository.findByExamAndGrade(examId, grade);
        for(ReportCardDesign reportCardDesign : reportCardDesigns) {
            ReportFieldType fieldType = reportCardDesign.getFieldType();
            if(fieldType.equals(ReportFieldType.MAIN) ||
                fieldType.equals(ReportFieldType.MANUAL_ENTRY) ||
                fieldType.equals(ReportFieldType.PERIODIC_TEST)) {
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
