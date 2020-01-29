package com.witcurve.service;

import com.witcurve.domain.enumeration.StaffType;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StaffService {

    StaffDTO create(StaffDTO staffDTO);

    StaffDTO update(StaffDTO staffDTO) throws WitcurveException;

    StaffDTO getStaffById(Long staffId) throws WitcurveException;

    StaffDTO getStaffByUserId(Long userId) throws WitcurveException;

    List<StaffDTO> getStaffBySchoolId(Long schoolId);

    List<StaffDTO> getStaffBySchoolInfoId(Long schoolInfoId, Boolean areClassTeacher, Boolean activated, StaffType type);

    StaffDTO getStaffByUsername(String username) throws WitcurveException;

    void deactivate(Long staffId) throws WitcurveException;

    void activate(Long staffId) throws WitcurveException;

    StaffDTO addProfilePhoto(Long studentId, MultipartFile file);
}
