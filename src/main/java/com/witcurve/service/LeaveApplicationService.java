package com.witcurve.service;

import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface LeaveApplicationService {

    LeaveApplicationDTO saveOrUpdate(LeaveApplicationDTO leaveApplicationDTOs, Boolean update) throws WitcurveException;

    LeaveApplicationDTO getLeaveApplicationById(Long leaveApplicationId) throws WitcurveException;

    void deleteLeaveApplication(Long leaveApplicationId) throws WitcurveException;

    LeaveApplicationDTO changeLeaveStatus(Long ApplicationId, Long StaffId, ApprovalStatus status, String note) throws WitcurveException;

    List<LeaveApplicationDTO> getLeaveApplicationsForStudent(Long studentId, Long sessionId, ApprovalStatus status) throws WitcurveException;

    List<LeaveApplicationDTO> getLeaveApplicationsForStaff(Long staffId, Long sessionId, ApprovalStatus status) throws WitcurveException;

    List<LeaveApplicationDTO> getLeaveApplicationsForStandard(Long standardId, ApprovalStatus status) throws WitcurveException;

    Long workingDays(LocalDate fromDate,LocalDate toDate, Long schoolInfoId, Boolean isSaturdayWorking) throws WitcurveException;

}
