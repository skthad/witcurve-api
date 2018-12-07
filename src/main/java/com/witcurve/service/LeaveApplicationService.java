package com.witcurve.service;

import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface LeaveApplicationService {

    LeaveApplicationDTO saveOrUpdate(LeaveApplicationDTO leaveApplicationDTOs) throws WitcurveException;

    LeaveApplicationDTO getLeaveApplicationById(Long leaveApplicationId) throws WitcurveException;

    void deleteLeaveApplication(Long leaveApplicationId) throws WitcurveException;

    LeaveApplicationDTO getLeaveApplicationApprover(Long ApplicationId, Long StaffId) throws WitcurveException;

    List<LeaveApplicationDTO> getLeaveApplicationsForStudent(Long studentId,Long sessionId, Boolean approved) throws WitcurveException;

    List<LeaveApplicationDTO> getLeaveApplicationsForStaff(Long studentId,Long sessionId, Boolean approved) throws WitcurveException;

    List<LeaveApplicationDTO> getLeaveApplicationsForStandard(Long standardId, Boolean approved) throws WitcurveException;

    Long getLeaveCount(Long leaveApplicationId,Long sessionId, Boolean isSaturdayWorking) throws WitcurveException;
}
