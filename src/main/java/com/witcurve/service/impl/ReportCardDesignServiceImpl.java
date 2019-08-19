package com.witcurve.service.impl;

import com.witcurve.domain.ReportCardDesign;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.domain.enumeration.ReportModelType;
import com.witcurve.repository.ReportCardDesignRepository;
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

    @Override
    public List<ReportCardDesignDTO> saveOrUpdate(List<ReportCardDesignDTO> reportCardDesignDTOS) {
        log.debug("Request to save ReportCardDesigns : {}", reportCardDesignDTOS);
        validAndFormatReportCardDesigns(reportCardDesignDTOS);
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

    private void validAndFormatReportCardDesigns(List<ReportCardDesignDTO> reportCardDesignDTOS) {
        Boolean mainRecordExists = false, remarksRecordExists =false, attendaceRecordExists = false;
        List<ReportCardDesign> existingReportCardDesigns = null;
        for(ReportCardDesignDTO reportCardDesignDTO : reportCardDesignDTOS) {
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
                    if(remarksRecordExists) {
                        throw new WitcurveException("There should be only one remarks field type record for a model");
                    }
                    existingReportCardDesigns = reportCardDesignRepository.findByModelTypeAndFieldTypeAndSchoolInfo(reportCardDesignDTO.getModelType(), ReportFieldType.REMARKS, reportCardDesignDTO.getSchoolInfoId());
                    if(existingReportCardDesigns.size()==0) {
                        if(reportCardDesignDTO.getId() != null) {
                            throw new WitcurveException("There doesn't exists a report card design of remarks field type to update with id : "+reportCardDesignDTO.getId());
                        }
                    } else if(existingReportCardDesigns.size() == 1) {
                        if(reportCardDesignDTO.getId() == null) {
                            throw new WitcurveException("There already exists a report card design remarks field type for this model, so another record cannot be created");
                        } else {
                            if(reportCardDesignDTO.getId() != existingReportCardDesigns.get(0).getId()) {
                                throw new WitcurveException("Id of the remarks field type : "+reportCardDesignDTO.getId()+" doesn't match with existing record");
                            }
                        }
                    } else {
                        throw new WitcurveException("There are multiple remarks type records stored for this model");
                    }
                    reportCardDesignDTO.setName("Remarks");
                    if(reportCardDesignDTO.getSelected() == null) {
                        throw new WitcurveException("Remarks field type record needs to have selected field value");
                    }
                    reportCardDesignDTO.setShortForm(null);
                    reportCardDesignDTO.setMarks(null);
                    reportCardDesignDTO.setShowGradesOnly(null);
                    remarksRecordExists = true;
                    break;
                case ATTENDANCE:
                    if(attendaceRecordExists) {
                        throw new WitcurveException("There should be only one attendance field type record for a model");
                    }
                    existingReportCardDesigns = reportCardDesignRepository.findByModelTypeAndFieldTypeAndSchoolInfo(reportCardDesignDTO.getModelType(), ReportFieldType.ATTENDANCE, reportCardDesignDTO.getSchoolInfoId());
                    if(existingReportCardDesigns.size()==0) {
                        if(reportCardDesignDTO.getId() != null) {
                            throw new WitcurveException("There doesn't exists a report card design of attendance field type to update with id : {}"+reportCardDesignDTO.getId());
                        }
                    } else if(existingReportCardDesigns.size() == 1) {
                        if(reportCardDesignDTO.getId() == null) {
                            throw new WitcurveException("There already exists a report card design attendance field type for this model, so another record cannot be created");
                        } else {
                            if(reportCardDesignDTO.getId() != existingReportCardDesigns.get(0).getId()) {
                                throw new WitcurveException("Id of the attendance field type : "+reportCardDesignDTO.getId()+" doesn't match with existing record");
                            }
                        }
                    } else {
                        throw new WitcurveException("There are multiple attendance type records stored for this model");
                    }
                    reportCardDesignDTO.setName("Attendance");
                    if(reportCardDesignDTO.getSelected() == null) {
                        throw new WitcurveException("Attendance field type record needs to selected field value");
                    }
                    reportCardDesignDTO.setShortForm(null);
                    reportCardDesignDTO.setMarks(null);
                    reportCardDesignDTO.setShowGradesOnly(null);
                    attendaceRecordExists = true;
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
