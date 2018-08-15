package com.witcurve.service.impl;

import afu.org.checkerframework.checker.oigj.qual.O;
import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.repository.GeneralSlotDetailsRepository;
import com.witcurve.service.GeneralSlotDetailsService;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.service.mapper.GeneralSlotDetailsMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class GeneralSlotDetailsServiceImpl implements GeneralSlotDetailsService {

    private final Logger log  = LoggerFactory.getLogger(GeneralSlotDetailsServiceImpl.class);

    @Autowired
    private GeneralSlotDetailsMapper generalSlotDetailsMapper;

    @Autowired
    private GeneralSlotDetailsRepository generalSlotDetailsRepository;

    @Override
    public GeneralSlotDetailsDTO saveOrUpdate(GeneralSlotDetailsDTO generalSlotDetailsDTO) {
        log.debug("Request to save or update generalSlotDetails : {}", generalSlotDetailsDTO);
        GeneralSlotDetails generalSlotDetails = generalSlotDetailsMapper.toEntity(generalSlotDetailsDTO);
        generalSlotDetails = generalSlotDetailsRepository.save(generalSlotDetails);
        return generalSlotDetailsMapper.toDto(generalSlotDetails);
    }

    @Override
    public GeneralSlotDetailsDTO getGeneralSlotDetailsById(Long generalSlotDetailsId) throws WitcurveException {
        log.debug("Request to get generalSlotDetail by id : {}", generalSlotDetailsId);
        GeneralSlotDetails generalSlotDetails = generalSlotDetailsRepository.findById(generalSlotDetailsId).get();
        if(generalSlotDetails == null) {
            throw new WitcurveException("No GeneraSlotDetails exist for given id");
        }
        return generalSlotDetailsMapper.toDto(generalSlotDetails);
    }

    @Override
    public void deleteGeneralSlotDetailsById(Long generalSlotDetailsId) throws WitcurveException {
        log.debug("Request to delete generalSlotDetails by id : {}", generalSlotDetailsId);
        GeneralSlotDetails generalSlotDetails = generalSlotDetailsRepository.findById(generalSlotDetailsId).get();
        if(generalSlotDetails == null) {
            throw new WitcurveException("No GeneraSlotDetails exist for given id");
        }
        generalSlotDetailsRepository.deleteById(generalSlotDetailsId);

    }




}
