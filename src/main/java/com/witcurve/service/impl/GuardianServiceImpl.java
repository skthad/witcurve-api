package com.witcurve.service.impl;

import com.witcurve.domain.Guardian;
import com.witcurve.repository.GuardianRepository;
import com.witcurve.service.GuardianService;
import com.witcurve.service.dto.GuardianDTO;
import com.witcurve.service.mapper.GuardianMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GuardianServiceImpl  implements GuardianService {

    private final Logger log  = LoggerFactory.getLogger(GuardianServiceImpl.class);

    @Autowired
    GuardianRepository guardianRepository;

    @Autowired
    GuardianMapper guardianMapper;


    @Override
    public GuardianDTO saveOrUpdate(GuardianDTO guardianDTO) {
        log.debug("Request to save or update guardian : {}", guardianDTO);
        Guardian guardian = guardianMapper.toEntity(guardianDTO);
        guardian = guardianRepository.save(guardian);
        return guardianMapper.toDto(guardian);
    }

    @Override
    public GuardianDTO getGuardianById(Long guardianId) throws WitcurveException {
        log.debug("Request to get guardian with id : {}", guardianId);
        Guardian guardian = guardianRepository.findById(guardianId).get();

        if (guardian ==  null) {
            throw new WitcurveException("No guardian exists with given id");
        }
        return guardianMapper.toDto(guardian);
    }

    @Override
    public void deleteGuardianById(Long guardianId) throws WitcurveException {
        log.debug("Request to delete guardian with id : {}", guardianId);
        Guardian guardian = guardianRepository.findById(guardianId).get();

        if (guardian ==  null) {
            throw new WitcurveException("No guardian exists with given id");
        }
        guardianRepository.delete(guardian);
    }
}
