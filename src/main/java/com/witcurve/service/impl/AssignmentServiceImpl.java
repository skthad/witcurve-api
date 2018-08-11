package com.witcurve.service.impl;

import com.witcurve.domain.Assignment;
import com.witcurve.repository.AssignmentRepository;
import com.witcurve.service.AssignmentService;
import com.witcurve.service.dto.AssignmentDTO;
import com.witcurve.service.mapper.AssignmentMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.witcurve.service.util.WitcurveUtil.getLocalDate;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private Logger log = LoggerFactory.getLogger(AssignmentServiceImpl.class);

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    AssignmentMapper assignmentMapper;

    @Override
    public AssignmentDTO saveOrUpdate(AssignmentDTO assignmentDTO) {
        log.debug("Request save to assignment {}", assignmentDTO);
        Assignment assignment = assignmentMapper.assignmentDTOToAssignment(assignmentDTO);
        assignment =  assignmentRepository.save(assignment);
        return assignmentMapper.assignmentToAssignmentDTO(assignment);
    }

    @Override
    public AssignmentDTO getAssignmentById(Long id) throws WitcurveException {
        log.debug("Request to get Assignment with id {}", id);
        Assignment assignment = assignmentRepository.findById(id).get();
        if (assignment ==  null) {
            throw new WitcurveException("No Assignment with given Id");
        }

        return assignmentMapper.assignmentToAssignmentDTO(assignment);
    }

    @Override
    public void deleteAssignment(Long assignmentId) throws WitcurveException {
        log.debug("Request to delete Assignment with id {}", assignmentId);
        Assignment assignment = assignmentRepository.findById(assignmentId).get();

        if (assignment == null){
            throw new WitcurveException("No assignment with given Id");
        }

        assignmentRepository.delete(assignment);
    }

    @Override
    public List<AssignmentDTO> findAssignmentsByPostedDateAndTimeTableUnitId(String postedDate, Long timeTableUnitId) {
        log.debug("Request to get assignments with postedDate : {}", postedDate);
        LocalDate localDate = getLocalDate(postedDate);
        List<Assignment> assignmentsPostedOnGivenDate = assignmentRepository.findAssignmentsByPostedDateAndTimeTableUnitId(localDate, timeTableUnitId);
        return assignmentMapper.assignmentToAssignmentDTOs(assignmentsPostedOnGivenDate);
    }

    @Override
    public List<AssignmentDTO> findAssignmentsBySubmissionDateAndTimeTableUnitId(String submissionDate, Long timeTableUnitId) {
        log.debug("Request to get assignments with submission date : {}", submissionDate);
        LocalDate localDate = getLocalDate(submissionDate);
        List<Assignment> assignmentsBySubmissionDate = assignmentRepository.findAssignmentsBySubmissionDateAndTimeTableUnitId(localDate, timeTableUnitId);
        return assignmentMapper.assignmentToAssignmentDTOs(assignmentsBySubmissionDate);
    }
}
