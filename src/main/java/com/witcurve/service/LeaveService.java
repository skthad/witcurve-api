package com.witcurve.service;

import com.witcurve.service.dto.LeaveDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface LeaveService {

    LeaveDTO saveOrUpdate(LeaveDTO leaveDTO);

    LeaveDTO getLeaveById(Long leaveId) throws WitcurveException;

    void deleteLeave(Long leaveId) throws WitcurveException;
}
