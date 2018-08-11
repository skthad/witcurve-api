package com.witcurve.service;

import com.witcurve.service.dto.AssignmentDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface AssignmentService {

    AssignmentDTO saveOrUpdate(AssignmentDTO assignmentDTO);

    AssignmentDTO getAssignmentById(Long id) throws WitcurveException;

    void deleteAssignment(Long assignmentId) throws WitcurveException;

    List<AssignmentDTO> findAssignmentsByPostedDateAndTimeTableUnitId(String postedDate, Long timeTableUnitId);

    List<AssignmentDTO> findAssignmentsBySubmissionDateAndTimeTableUnitId(String submissionDate, Long timeTableUnitId);
}
