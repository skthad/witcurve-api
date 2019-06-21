package com.witcurve.service.impl;

import com.witcurve.domain.Staff;
import com.witcurve.domain.Student;
import com.witcurve.domain.SurveyForm;
import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.domain.enumeration.SurveyUserType;
import com.witcurve.repository.StaffRepository;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.SurveyFormRepository;
import com.witcurve.repository.SurveySubmissionRepository;
import com.witcurve.service.SurveyFormService;
import com.witcurve.service.dto.SurveyFormDTO;
import com.witcurve.service.mapper.SurveyFormMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SurveyFormServiceImpl implements SurveyFormService {

    private final Logger log  = LoggerFactory.getLogger(SurveyFormServiceImpl.class);

    @Autowired
    SurveyFormMapper surveyFormMapper;

    @Autowired
    SurveyFormRepository surveyFormRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    SurveySubmissionRepository surveySubmissionRepository;

    @Override
    public SurveyFormDTO saveOrUpdate(SurveyFormDTO surveyFormDTO) throws WitcurveException {
        log.debug("Request to save or update surveyForm : {}",surveyFormDTO);
        SurveyForm surveyForm = surveyFormMapper.toEntity(surveyFormDTO);
        surveyForm = surveyFormRepository.save(surveyForm);
        return surveyFormMapper.toDto(surveyForm);
    }

    @Override
    public SurveyFormDTO getOne(Long surveyFormId) throws WitcurveException {
        log.debug("Request to get surveyForm with id : {}", surveyFormId);
        Optional<SurveyForm> surveyForm = surveyFormRepository.findById(surveyFormId);
        if(!surveyForm.isPresent()) {
            throw new WitcurveException("No survey formFound with id : "+surveyFormId);
        }
        return surveyFormMapper.toDto(surveyForm.get());
    }

    public List<SurveyFormDTO> findAll(Long schoolInfoId, SurveyFormCreator creator, List<SurveyFormStatus> statusList) {
        log.debug("Request to get surveyForms for schoolInfo with id : {} of creator : {} of statuses : {}", schoolInfoId, creator, statusList);
        if(statusList == null) {
            statusList = new ArrayList<>(EnumSet.allOf(SurveyFormStatus.class));
        }
        List<SurveyForm> result = surveyFormRepository.findBySchoolInfoIdAndCreatorAndStatusList(schoolInfoId, creator, statusList);
        return surveyFormMapper.toDto(result);
    }

    public List<SurveyFormDTO> findSurveyFormsForStudentId(Long studentId) throws WitcurveException {
        log.debug("Request to get surveyForms for student with id : {}", studentId);
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new WitcurveException("No student with given id");
        }
        List<SurveyForm> surveyFormList = surveyFormRepository.findBySchoolInfoIdAndTypesAndStatusList(student.get().getSchoolInfo().getId(),
            Arrays.asList(SurveyUserType.ALL, SurveyUserType.PARENT), Arrays.asList(SurveyFormStatus.PUBLISHED));
        List<SurveyFormDTO> result = surveyFormMapper.toDto(surveyFormList);
        updateSubmitStatus(result, student.get().getUser().getId());
        return result;
    }

    public List<SurveyFormDTO> findSurveyFormsForStaffId(Long staffId) throws WitcurveException {
        log.debug("Request to get surveyForms for staff with id : {}", staffId);
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (!staff.isPresent()) {
            throw new WitcurveException("No staff with given id");
        }
        List<SurveyForm> surveyFormList = surveyFormRepository.findBySchoolInfoIdAndTypesAndStatusList(staff.get().getSchoolInfo().getId(),
            Arrays.asList(SurveyUserType.ALL, SurveyUserType.TEACHING_STAFF), Arrays.asList(SurveyFormStatus.PUBLISHED));
        List<SurveyFormDTO> result = surveyFormMapper.toDto(surveyFormList);
        updateSubmitStatus(result, staff.get().getUser().getId());
        return result;
    }

    public void deleteOne(Long surveyFormId) throws WitcurveException {
        log.debug("Request to delete surveyForm with id : {}", surveyFormId);
        Optional<SurveyForm> surveyForm = surveyFormRepository.findById(surveyFormId);
        if(!surveyForm.isPresent()) {
            throw new WitcurveException("No surveyForm found with id : "+surveyFormId);
        }
        surveyFormRepository.delete(surveyForm.get());
    }

    private void updateSubmitStatus(List<SurveyFormDTO> surveyForms, Long userId) {
        for(SurveyFormDTO surveyForm : surveyForms) {
            List<Long> formIds = surveySubmissionRepository.findByUserId(userId);
            if(!formIds.contains(surveyForm.getId())) {
                surveyForm.setUserSubmitted(false);
            } else {
                surveyForm.setUserSubmitted(true);
            }
        }
    }


}
