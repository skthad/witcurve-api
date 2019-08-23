package com.witcurve.service;

import com.witcurve.domain.Authority;
import com.witcurve.service.dto.AuthorityDTO;
import com.witcurve.service.dto.InstituteDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InstituteService {

    InstituteDTO saveOrUpdate(InstituteDTO instituteDTO);

    InstituteDTO getInstituteById(Long instituteId) throws WitcurveException;

    InstituteDTO addAttachment(Long instituteId, MultipartFile file);

    List<InstituteDTO> getAllInstitutes();

    void deleteInstitute(Long instituteId) throws WitcurveException;

    Authority saveOrUpdateCustomAuthority(Long instituteId, AuthorityDTO authorityDTO);

    void deleteCustomAuthority(Long instituteId, String name);

    List<Authority> getAuthorities(Long instituteId);

    InstituteDTO getInstituteMapBySubDomainName(String subDomainName) throws WitcurveException;
}
