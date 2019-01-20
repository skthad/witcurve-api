package com.witcurve.service;

import com.witcurve.domain.Institute;
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

}
