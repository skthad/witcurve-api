package com.witcurve.service;

import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface LeaveApplicationService {

    LeaveApplicationDTO saveOrUpdate(LeaveApplicationDTO leaveApplicationDTOs, Boolean update) throws WitcurveException;

    LeaveApplicationDTO getLeaveApplicationById(Long leaveApplicationId) throws WitcurveException;

    void deleteLeaveApplication(Long leaveApplicationId) throws WitcurveException;

    LeaveApplicationDTO changeLeaveStatus(Long ApplicationId, Long StaffId, ApprovalStatus status, String note) throws WitcurveException;

    Long workingDays(LocalDate fromDate,LocalDate toDate, Long schoolInfoId, Boolean isSaturdayWorking) throws WitcurveException;

    Map<Long, List<LeaveApplicationDTO>> getLeaveDetails(Long studentId, Long staffId, Long standardId, Long schoolInfoId, LocalDate fromDate, LocalDate toDate, ApprovalStatus status) throws WitcurveException ;

}
