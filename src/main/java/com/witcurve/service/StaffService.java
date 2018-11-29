package com.witcurve.service;

import com.witcurve.service.dto.StaffDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface StaffService {

    StaffDTO saveOrUpdate(StaffDTO staffDTO);

    StaffDTO getStaffById(Long staffId) throws WitcurveException;

    void deleteStaffById(Long staffId) throws WitcurveException;

    List<StaffDTO> getSubstituteList(Long staffId, Long gsdId, LocalDate date) throws WitcurveException;
}
