package com.witcurve.service;

import com.witcurve.domain.enumeration.UserType;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface StaffService {

    StaffDTO create(StaffDTO staffDTO, UserType type, List<String> authorities);

    StaffDTO update(StaffDTO staffDTO, UserType type, List<String> authorities) throws WitcurveException;

    StaffDTO getStaffById(Long staffId) throws WitcurveException;

    StaffDTO getStaffByUserId(Long userId) throws WitcurveException;

    void deleteStaffById(Long staffId) throws WitcurveException;

    List<StaffDTO> getStaffBySchoolId(Long schoolId);

    List<StaffDTO> getStaffBySchoolInfoId(Long schoolInfoId, Boolean areClassTeacher);

    StaffDTO getStaffByUsername(String username) throws WitcurveException;
}
