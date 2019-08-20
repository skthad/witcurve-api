package com.witcurve.service.impl;

import com.witcurve.domain.Event;
import com.witcurve.domain.ExamCourseDetails;
import com.witcurve.domain.ReportCardDesign;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.domain.enumeration.ReportModelType;
import com.witcurve.repository.EventRepository;
import com.witcurve.repository.ExamCourseDetailsRepository;
import com.witcurve.repository.ReportCardDesignRepository;
import com.witcurve.repository.SchoolInfoRepository;
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
    ExamCourseDetailsRepository examCourseDetailsRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Override
    public List<ReportCardDesignDTO> saveOrUpdate(List<ReportCardDesignDTO> reportCardDesignDTOS, Long schoolInfoId) {
        log.debug("Request to save ReportCardDesigns : {}", reportCardDesignDTOS);
        validAndFormatReportCardDesigns(reportCardDesignDTOS, schoolInfoId);
        List<ReportCardDesign> reportCardDesigns = reportCardDesignMapper.toEntity(reportCardDesignDTOS);
        reportCardDesigns = reportCardDesignRepository.saveAll(reportCardDesigns);
        return reportCardDesignMapper.toDto(reportCardDesigns);
    }

    @Override
    public List<ReportCardDesignDTO> findByModelTypeAndSchoolInfoId(ReportModelType modelType, Long schoolInfoId, ReportFieldType fieldType) {
        log.debug("Request to get ReportCardDesigns of model type : {}, field type : {} for school info with id : {}", modelType, fieldType, schoolInfoId );
        List<ReportCardDesign> result;
        if(fieldType == null) {
            result = reportCardDesignRepository.findByModelTypeAndSchoolInfo(modelType, schoolInfoId);
        } else {
            result = reportCardDesignRepository.findByModelTypeAndFieldTypeAndSchoolInfo(modelType, fieldType, schoolInfoId);
        }
        return reportCardDesignMapper.toDto(result);
    }

    @Override
    public List<ReportCardDesignDTO> findManualEntryFieldsByEcdIdOrEventId(ReportModelType modelType, Long id) {
        log.debug("Request to get ReportCardDesigns of model type : {} with id : {}", modelType, id);
        List<ReportCardDesign> result;
        Long schoolInfoId;
        //todo add a logic, if report card are generated, get the manual entry fields by student marks
        if(modelType.equals(ReportModelType.EXAM)) {
            Optional<ExamCourseDetails> examCourseDetails = examCourseDetailsRepository.findById(id);
            if(!examCourseDetails.isPresent()) {
                throw new WitcurveException("No exam course details exists for given id");
            }
            schoolInfoId = examCourseDetails.get().getGsd().getExam().getSchoolInfo().getId();
        } else {
            Optional<Event> event = eventRepository.findById(id);
            if(!event.isPresent()) {
                throw new WitcurveException("No event found with given id");
            }
            if(!event.get().getType().equals(EventType.PERIODIC_TEST)) {
                throw new WitcurveException("Manual Entry fields are available only for Periodic Tests");
            }
            schoolInfoId = event.get().getSchoolInfo().getId();
        }
        result = reportCardDesignRepository.findByModelTypeAndFieldTypeAndSchoolInfo(modelType, ReportFieldType.MANUAL_ENTRY, schoolInfoId);
        return reportCardDesignMapper.toDto(result);
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
        //todo delete student marks which are entered and not been generated yet
        reportCardDesignRepository.deactivateReportCardDesigns(ids);
    }

    private void validAndFormatReportCardDesigns(List<ReportCardDesignDTO> reportCardDesignDTOS, Long schoolInfoId) {
        Boolean mainRecordExists = false, remarksRecordExists =false, attendaceRecordExists = false, finalGradeRecordExists = false;
        List<ReportCardDesign> existingReportCardDesigns = null;
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        if(!schoolInfo.isPresent()) {
            throw new WitcurveException("School Info doesn't exist with id : "+schoolInfoId);
        }
        for(ReportCardDesignDTO reportCardDesignDTO : reportCardDesignDTOS) {
            reportCardDesignDTO.setSchoolInfoId(schoolInfoId);
            switch (reportCardDesignDTO.getFieldType()) {
                case MAIN:
                    if(mainRecordExists) {
                        throw new WitcurveException("There should be only one main field type record for a model");
                    }
                    existingReportCardDesigns = reportCardDesignRepository.findByModelTypeAndFieldTypeAndSchoolInfo(reportCardDesignDTO.getModelType(), ReportFieldType.MAIN, reportCardDesignDTO.getSchoolInfoId());
                    if(existingReportCardDesigns.size()==0) {
                        if(reportCardDesignDTO.getId() != null) {
                            throw new WitcurveException("There doesn't exists a report card design of main field type to update with given id : "+reportCardDesignDTO.getId());
                        }
                    } else if(existingReportCardDesigns.size() == 1) {
                        if(reportCardDesignDTO.getId() == null) {
                            throw new WitcurveException("There already exists a report card design main field type for this model, so another record cannot be created");
                        } else {
                            if(reportCardDesignDTO.getId() != existingReportCardDesigns.get(0).getId()) {
                                throw new WitcurveException("Id of the main field type with id "+reportCardDesignDTO.getId()+" doesn't match with existing record");
                            }
                        }
                    } else {
                        throw new WitcurveException("There are multiple main type records stored for this model");
                    }
                    if(reportCardDesignDTO.getModelType().equals(ReportModelType.EXAM)) {
                        reportCardDesignDTO.setName("Exam");
                        reportCardDesignDTO.setShortForm("Exam");
                    } else {
                        reportCardDesignDTO.setName("Periodic Test");
                        reportCardDesignDTO.setShortForm("P.T.");
                    }
                    if(reportCardDesignDTO.getMarks() == null || reportCardDesignDTO.getShowGradesOnly() == null) {
                        throw new WitcurveException("Main field type record needs to have marks and show grade option values");
                    }
                    reportCardDesignDTO.setSelected(false);
                    mainRecordExists = true;
                    break;
                case REMARKS:
                case ATTENDANCE:
                case FINAL_GRADE_ONLY:
                    ReportFieldType fieldType = reportCardDesignDTO.getFieldType();
                    if(fieldType.equals(ReportFieldType.REMARKS)) {
                        if(remarksRecordExists) {
                            throw new WitcurveException("There should be only one remarks field type record for a model");
                        }
                    } else if(fieldType.equals(ReportFieldType.ATTENDANCE)) {
                        if(attendaceRecordExists) {
                            throw new WitcurveException("There should be only one attendance field type record for a model");
                        }
                    } else {
                        if(finalGradeRecordExists) {
                            throw new WitcurveException("There should be only one final grade only field type record for a model");
                        }
                    }

                    existingReportCardDesigns = reportCardDesignRepository.findByModelTypeAndFieldTypeAndSchoolInfo(reportCardDesignDTO.getModelType(), fieldType, reportCardDesignDTO.getSchoolInfoId());
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
                    reportCardDesignDTO.setName("Remarks");
                    if(reportCardDesignDTO.getSelected() == null) {
                        throw new WitcurveException(fieldType.toString().toLowerCase()+" field type record needs to have selected field value");
                    }
                    reportCardDesignDTO.setShortForm(null);
                    reportCardDesignDTO.setMarks(null);
                    reportCardDesignDTO.setShowGradesOnly(null);
                    if(fieldType.equals(ReportFieldType.REMARKS)) {
                        remarksRecordExists = true;
                    } else if(fieldType.equals(ReportFieldType.ATTENDANCE)) {
                        attendaceRecordExists = true;
                    } else {
                        finalGradeRecordExists = true;
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
                    if(reportCardDesignDTO.getShortForm() == null || reportCardDesignDTO.getMarks() == null || reportCardDesignDTO.getShowGradesOnly() == null || reportCardDesignDTO.getOrder()== null) {
                        throw new WitcurveException("Manual Entry field type record needs to have shortForm, marks, order and show grade only values");
                    }
                    reportCardDesignDTO.setSelected(null);
                    break;
            }
        }
    }

}
