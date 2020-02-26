package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.QuestionType;
import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.domain.enumeration.SurveyUserType;
import com.witcurve.repository.*;
import com.witcurve.service.SnsService;
import com.witcurve.service.SurveyFormService;
import com.witcurve.service.SurveySectionService;
import com.witcurve.service.dto.SurveyFormDTO;
import com.witcurve.service.mapper.SurveyFormMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.SummaryVM;
import com.witcurve.web.rest.vm.SurveyQuestionAnswerCountVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class SurveyFormServiceImpl implements SurveyFormService {

    private final Logger log = LoggerFactory.getLogger(SurveyFormServiceImpl.class);

    private final List<String> SUMMARY_LIST = Arrays.asList(QuestionType.RATING.toString(), QuestionType.DICHOTOMOUS.toString(), QuestionType.SINGLE_CHOICE.toString(), QuestionType.MULTIPLE_CHOICE.toString());

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

    @Autowired
    SnsService snsService;

    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    SurveySectionRepository surveySectionRepository;

    @Autowired
    SurveySectionService surveySectionService;

    @Autowired
    SurveyAnswerRepository surveyAnswerRepository;

    @Override
    public SurveyFormDTO saveOrUpdate(SurveyFormDTO surveyFormDTO) throws WitcurveException {
        log.debug("Request to save or update surveyForm : {}", surveyFormDTO);
        isValid(surveyFormDTO);
        SurveyForm surveyForm = surveyFormMapper.toEntity(surveyFormDTO);
        surveyForm = surveyFormRepository.save(surveyForm);
        return surveyFormMapper.toDto(surveyForm);
    }

    @Override
    public SurveyFormDTO getOne(Long surveyFormId) throws WitcurveException {
        log.debug("Request to get surveyForm with id : {}", surveyFormId);
        Optional<SurveyForm> surveyForm = surveyFormRepository.findById(surveyFormId);
        if (!surveyForm.isPresent()) {
            throw new WitcurveException("No survey form found with id : " + surveyFormId);
        }
        return surveyFormMapper.toDto(surveyForm.get());
    }

    @Override
    public List<SurveyFormDTO> findAll(Long schoolInfoId, SurveyFormCreator creator, List<SurveyFormStatus> statusList) {
        log.debug("Request to get surveyForms for schoolInfo with id : {} of creator : {} of statuses : {}", schoolInfoId, creator, statusList);
        if (statusList == null) {
            statusList = new ArrayList<>(EnumSet.allOf(SurveyFormStatus.class));
        }
        List<SurveyForm> result = surveyFormRepository.findBySchoolInfoIdAndCreatorAndStatusList(schoolInfoId, creator, statusList);
        return surveyFormMapper.toDto(result);
    }

    @Override
    public List<SurveyFormDTO> findSurveyFormsForStudentId(Long studentId) throws WitcurveException {
        log.debug("Request to get surveyForms for student with id : {}", studentId);
        Optional<Student> student = studentRepository.findById(studentId);
        if (!student.isPresent()) {
            throw new WitcurveException("No student present with given id");
        }
        Set<StudentStandard> studentStandards = student.get().getStudentStandards();
        List<SurveyForm> surveyFormList;
        if (studentStandards != null && studentStandards.size() > 0) {
            StudentStandard studentStandard = student.get().getStudentStandards().stream().collect(Collectors.toList()).get(0);
            surveyFormList = surveyFormRepository.findBySchoolInfoIdAndStatusAndStandardId(student.get().getSchoolInfo().getId(), Arrays.asList(SurveyFormStatus.PUBLISHED), studentStandard.getStandard().getId());
        } else {
            throw new WitcurveException("Student does not belong to any standard");
        }
        List<SurveyFormDTO> result = surveyFormMapper.toDto(surveyFormList);
        updateSubmitStatus(result, student.get().getUser().getId());
        return result;
    }

    @Override
    public List<SurveyFormDTO> findSurveyFormsForStaffId(Long staffId) throws WitcurveException {
        log.debug("Request to get surveyForms for staff with id : {}", staffId);
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (!staff.isPresent()) {
            throw new WitcurveException("No staff with given id");
        }
        List<SurveyForm> surveyFormList = surveyFormRepository.findBySchoolInfoIdAndTypesAndStatusList(staff.get().getSchoolInfo().getId(),
            Arrays.asList(SurveyUserType.ALL, SurveyUserType.STAFF), Arrays.asList(SurveyFormStatus.PUBLISHED));
        List<SurveyFormDTO> result = surveyFormMapper.toDto(surveyFormList);
        updateSubmitStatus(result, staff.get().getUser().getId());
        return result;
    }

    @Override
    public void deleteOne(Long surveyFormId) throws WitcurveException {
        log.debug("Request to delete surveyForm with id : {}", surveyFormId);
        Optional<SurveyForm> surveyForm = surveyFormRepository.findById(surveyFormId);
        if (!surveyForm.isPresent()) {
            throw new WitcurveException("No surveyForm found with id : " + surveyFormId);
        }
        for (SurveySection surveySection : surveyForm.get().getSections()) {
            surveySectionService.deleteOne(surveySection.getId());
        }
        surveyFormRepository.delete(surveyForm.get());
    }

    @Override
    public SurveyFormDTO updateSurveyFormStatus(Long surveyFormId, SurveyFormStatus status) {
        log.debug("Request to update surveyForm  status with id : {}", surveyFormId);
        Optional<SurveyForm> surveyForm = surveyFormRepository.findById(surveyFormId);
        if (!surveyForm.isPresent()) {
            throw new WitcurveException("No surveyForm found with id : " + surveyFormId);
        }
        surveyForm.get().setStatus(status);
        SurveyFormDTO surveyFormDTO = surveyFormMapper.toDto(surveyForm.get());
        snsService.sendPushNotificationOnFormPublish(surveyFormDTO);
        return surveyFormDTO;
    }

    @Override
    public Map<Long, SummaryVM> getSurveySummary(Long formId) {
        Map<Long, SummaryVM> mapOfQuestionAnswersAndCount = new LinkedHashMap<>();
        List<SurveyQuestionAnswerCountVM> surveyQuestionAnswerCounts = surveyAnswerRepository.countForForm(SUMMARY_LIST, formId);
        for (SurveyQuestionAnswerCountVM surveyQuestionAnswerCount : surveyQuestionAnswerCounts) {
            if (mapOfQuestionAnswersAndCount.containsKey(surveyQuestionAnswerCount.getQuestionId())) {
                Map<String, Long> answerAndCount = mapOfQuestionAnswersAndCount.get(surveyQuestionAnswerCount.getQuestionId()).getMapOfAnswerAndCount();
                answerAndCount.put(surveyQuestionAnswerCount.getAnswer(), surveyQuestionAnswerCount.getCount());
            } else {
                Map<String, Long> mapOfAnswerAndCount = new LinkedHashMap<>();
                mapOfAnswerAndCount.put(surveyQuestionAnswerCount.getAnswer(), surveyQuestionAnswerCount.getCount());
                SummaryVM summaryVM = new SummaryVM();
                summaryVM.setMapOfAnswerAndCount(mapOfAnswerAndCount);
                mapOfQuestionAnswersAndCount.put(surveyQuestionAnswerCount.getQuestionId(), summaryVM);
            }
        }
        List<SurveyQuestionAnswerCountVM> surveyQuestionAnswerCountsForLongAndShortAnswer = surveyAnswerRepository.countForLongAndShortAnswerTypeQue(Arrays.asList(QuestionType.LONG_ANSWER, QuestionType.SHORT_ANSWER), formId);
        for (SurveyQuestionAnswerCountVM surveyQuestionAnswerCount : surveyQuestionAnswerCountsForLongAndShortAnswer) {
            SummaryVM summaryVM = new SummaryVM();
            summaryVM.setCount(surveyQuestionAnswerCount.getCount());
            mapOfQuestionAnswersAndCount.put(surveyQuestionAnswerCount.getQuestionId(), summaryVM);
        }
        return mapOfQuestionAnswersAndCount;
    }

    private void updateSubmitStatus(List<SurveyFormDTO> surveyForms, Long userId) {
        List<Long> formIds = surveySubmissionRepository.findByUserId(userId);
        for (SurveyFormDTO surveyForm : surveyForms) {
            if (!formIds.contains(surveyForm.getId())) {
                surveyForm.setUserSubmitted(false);
            } else {
                surveyForm.setUserSubmitted(true);
            }
        }
    }

    private void isValid(SurveyFormDTO surveyFormDTO) {
        if (surveyFormDTO.getId() != null) {
            Optional<SurveyForm> surveyForm = surveyFormRepository.findById(surveyFormDTO.getId());
            if (!surveyForm.isPresent()) {
                throw new WitcurveException("No survey form is present with id : " + surveyFormDTO.getId());
            }
            if (!surveyForm.get().getType().equals(surveyFormDTO.getType())) {
                throw new WitcurveException("Type of user can not be changed in update request");
            }
            List<Long> standardIdsInForm = null;
            if (surveyForm.get().getStandards().size() > 0) {
                standardIdsInForm = surveyForm.get().getStandards().stream().map(Standard::getId).collect(Collectors.toList());
            }
            List<Long> idsInDTO = null;
            if (surveyFormDTO.getStandardIds() != null) {
                if (surveyFormDTO.getStandardIds().size() > 0) {
                    idsInDTO = surveyFormDTO.getStandardIds();
                }
            }
            if (standardIdsInForm == null) {
                if (idsInDTO != null) {
                    throw new WitcurveException("Standard can not be added or deleted in update request");
                }
            } else {
                if (idsInDTO == null) {
                    throw new WitcurveException("Standard can not be added or deleted in update request");
                }
                if (!idsInDTO.containsAll(standardIdsInForm) || !standardIdsInForm.containsAll(idsInDTO)) {
                    throw new WitcurveException("Standard can not be added or deleted in update request");
                }
            }
        }
    }
}




