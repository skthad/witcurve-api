package com.witcurve.service;

import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface SchoolService {

    SchoolDTO saveOrUpdate(SchoolDTO schoolDTO) throws WitcurveException;

    SchoolDTO getSchoolById(Long schoolId) throws WitcurveException;

    void deleteSchool(Long schoolId) throws WitcurveException;

    List<SchoolDTO> getSchoolByInstituteId(Long instituteId);

    SchoolDTO convertToPrimaryBranch(Long schoolId);
}
