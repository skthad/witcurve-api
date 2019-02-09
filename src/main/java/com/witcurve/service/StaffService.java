package com.witcurve.service;

import com.witcurve.service.dto.StaffDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface StaffService {

    StaffDTO create(StaffDTO staffDTO);

    StaffDTO update(StaffDTO staffDTO);

    StaffDTO getStaffById(Long staffId) throws WitcurveException;

    StaffDTO getStaffByUserId(Long userId) throws WitcurveException;

    void deleteStaffById(Long staffId) throws WitcurveException;

    List<StaffDTO> getStaffBySchoolId(Long schoolId);

    List<StaffDTO> getStaffBySchoolInfoId(Long schoolInfoId);

    StaffDTO getStaffByUsername(String username) throws WitcurveException;
}
