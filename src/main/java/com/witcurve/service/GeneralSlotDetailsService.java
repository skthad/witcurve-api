package com.witcurve.service;

import com.witcurve.domain.enumeration.GSDStatus;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface GeneralSlotDetailsService {

    List<GeneralSlotDetailsDTO> createGSDs(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) throws WitcurveException;

    List<GeneralSlotDetailsDTO> createOrUpdateExamSlots(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) throws WitcurveException;

    List<GeneralSlotDetailsDTO> update(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs);

    void cloneGSDs(Long sourceStandardId, List<Long> destinationStandardIds) throws WitcurveException;

    void cloneExamSlots(Grade sourceGrade, List<Grade> destinationGrades, Long examId) throws WitcurveException;

    void deactivateGSDsForStandards(List<Long> standardIds);

    void activateGSDsForStandard(Long standardId, String bindingId);

    GeneralSlotDetailsDTO getGeneralSlotDetailsById(Long generalSlotDetailsId) throws WitcurveException;

    List<GeneralSlotDetailsDTO> getGeneralSlotDetailsByStandardId(Long standardId, GSDStatus status) throws WitcurveException;

    List<GeneralSlotDetailsDTO> getExamSlotsByGradeAndExam(Grade grade, Long examId) throws WitcurveException;

    void deleteGSDsByBindingId(String bindingId) throws WitcurveException;

    void deleteExamSlotsByGradeAndExamId(Grade grade, Long examId) throws WitcurveException;
}
