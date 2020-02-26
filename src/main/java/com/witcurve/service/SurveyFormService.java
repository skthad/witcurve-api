package com.witcurve.service;

import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.service.dto.SurveyFormDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.SummaryVM;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface SurveyFormService {

    SurveyFormDTO saveOrUpdate(SurveyFormDTO surveyFormDTO) throws WitcurveException;

    SurveyFormDTO getOne(Long surveyFormId) throws WitcurveException;

    List<SurveyFormDTO> findAll(Long schoolInfoId, SurveyFormCreator creator, List<SurveyFormStatus> statusList);

    List<SurveyFormDTO> findSurveyFormsForStudentId(Long studentId);

    List<SurveyFormDTO> findSurveyFormsForStaffId(Long staffId);

    SurveyFormDTO updateSurveyFormStatus(Long surveyFormId, SurveyFormStatus status);

    void deleteOne(Long surveyFormId);

    Map<Long, SummaryVM> getSurveySummary(Long formId);
}
