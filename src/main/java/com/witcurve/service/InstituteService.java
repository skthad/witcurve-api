package com.witcurve.service;

import com.witcurve.domain.Authority;
import com.witcurve.service.dto.InstituteDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface InstituteService {

    InstituteDTO saveOrUpdate(InstituteDTO instituteDTO);

    InstituteDTO getInstituteById(Long instituteId) throws WitcurveException;

    List<InstituteDTO> getAllInstitutes();

    void deleteInstitute(Long instituteId) throws WitcurveException;

    Authority saveOrUpdateCustomAuthority(Long instituteId, Authority authority);

    void deleteCustomAuthority(Long instituteId, String name);

    List<Authority> getAuthorities(Long instituteId);
}
