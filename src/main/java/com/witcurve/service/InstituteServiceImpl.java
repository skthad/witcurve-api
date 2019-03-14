package com.witcurve.service;

import com.witcurve.domain.Authority;
import com.witcurve.domain.Institute;
import com.witcurve.repository.AuthorityRepository;
import com.witcurve.repository.InstituteRepository;
import com.witcurve.service.dto.InstituteDTO;
import com.witcurve.service.mapper.InstituteMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InstituteServiceImpl implements InstituteService {

    private final Logger log = LoggerFactory.getLogger(InstituteServiceImpl.class);

    @Autowired
    InstituteRepository instituteRepository;

    @Autowired
    InstituteMapper instituteMapper;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public InstituteDTO saveOrUpdate(InstituteDTO instituteDTO) {
        log.debug("Request to save or update institute");
        Institute institute = instituteMapper.toEntity(instituteDTO);
        institute = instituteRepository.save(institute);
        return instituteMapper.toDto(institute);
    }

    @Override
    public InstituteDTO getInstituteById(Long instituteId) throws WitcurveException {
        log.debug("Request to get institute with id: {}", instituteId);
        Optional<Institute> institute = instituteRepository.findById(instituteId);
        if (!institute.isPresent()) {
            throw new WitcurveException("No Institute with given id " + instituteId);
        }
        return instituteMapper.toDto(institute.get());
    }

    @Override
    public List<InstituteDTO> getAllInstitutes() {
        log.debug("Request to get all institutes");
        List<Institute> institutes = instituteRepository.findAll();
        return instituteMapper.toDto(institutes);
    }

    @Override
    public void deleteInstitute(Long instituteId) throws WitcurveException {
        log.debug("Request to delete institute with id {}", instituteId);
        Optional<Institute> institute = instituteRepository.findById(instituteId);
        if (!institute.isPresent()) {
            throw new WitcurveException("No Institute with given id " + instituteId);
        }
        instituteRepository.delete(institute.get());
    }

    @Override
    public Authority saveOrUpdateCustomAuthority(Long instituteId, Authority authority) {
        // check if current logged in user belongs to the input institute
        // create custom dto later used to show default vs custom authorities separately


        authority.setInstituteId(instituteId);
        if (authority.getName() == null) {
            authority.setName(instituteId + "-ROLE_" + authority.getDisplayName().toUpperCase().replaceAll("[ ]+", "_"));
        }

        return authorityRepository.save(authority);
    }

    @Override
    public void deleteCustomAuthority(Long instituteId, String name) {
        if (name.startsWith(instituteId + "-")) {
            authorityRepository.deleteByName(name);
        }
    }

    @Override
    public List<Authority> getAuthorities(Long instituteId) {
        return authorityRepository.getByInstituteId(instituteId);
    }

}
