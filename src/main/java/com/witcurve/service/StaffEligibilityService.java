package com.witcurve.service;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.dto.StaffEligibilityDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface StaffEligibilityService {

    StaffEligibilityDTO saveOrUpdate(StaffEligibilityDTO staffEligibilityDTO) throws WitcurveException;

    List<StaffEligibilityDTO> getStaffEligibilitysBySchoolInfo(Long schoolInfoId, String subject, Grade grade) throws WitcurveException;

    List<StaffEligibilityDTO> getStaffEligibilitysByStaff(Long staffId, String subject, Grade grade) throws WitcurveException;

    void deleteStaffEligibility(Long staffEligibilityId) throws WitcurveException;
}
