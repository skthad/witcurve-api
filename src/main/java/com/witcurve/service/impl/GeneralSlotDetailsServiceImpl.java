package com.witcurve.service.impl;

import com.witcurve.domain.Standard;
import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.repository.StandardRepository;
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

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class GeneralSlotDetailsServiceImpl implements GeneralSlotDetailsService {

    private final Logger log  = LoggerFactory.getLogger(GeneralSlotDetailsServiceImpl.class);

    @Autowired
    private GeneralSlotDetailsMapper generalSlotDetailsMapper;

    @Autowired
    private GeneralSlotDetailsRepository generalSlotDetailsRepository;

    @Autowired
    private StandardRepository standardRepository;

    @Override
    public List<GeneralSlotDetailsDTO> saveOrUpdate(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) {
        log.debug("Request to save or update generalSlotDetails");
        List<GeneralSlotDetails> generalSlotDetails = generalSlotDetailsMapper.toEntity(generalSlotDetailsDTOs);
        List<GeneralSlotDetailsDTO> slots = generalSlotDetailsMapper.toDto(generalSlotDetailsRepository.saveAll(generalSlotDetails));
        return slots;
    }

    @Override
    public void clone(Long sourceStandardId, List<Long> destinationStandardIds) {
        log.debug("Request to clone generalSlotDetails ");
        List<GeneralSlotDetails> slots = generalSlotDetailsRepository.findByStandardIdAndExamIdNullOrderByStart(sourceStandardId);

        for (Long destinationStandardId : destinationStandardIds) {
            List<GeneralSlotDetails> slotsToCreate = slots.stream().collect(Collectors.toList());
            Standard standard = standardRepository.findById(destinationStandardId).get();
            for (GeneralSlotDetails slot : slotsToCreate) {
                slot.setId(null);
                slot.setStandard(standard);
            }
            generalSlotDetailsRepository.saveAll(slotsToCreate);
        }

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
    public void deleteGeneralSlotDetails(Long generalSlotDetailsId) throws WitcurveException {
        log.debug("Request to delete generalSlotDetails by id : {}", generalSlotDetailsId);
        GeneralSlotDetails generalSlotDetails = generalSlotDetailsRepository.findById(generalSlotDetailsId).get();
        if(generalSlotDetails == null) {
            throw new WitcurveException("No GeneraSlotDetails exist for given id");
        }
        generalSlotDetailsRepository.deleteById(generalSlotDetailsId);

    }

    @Override
    public List<GeneralSlotDetailsDTO> getGeneralSlotDetailsByStandardId(Long standardId) throws WitcurveException {
        log.debug("Request to get generalSlotDetails by standard id : {}", standardId);
        List<GeneralSlotDetails> generalSlotDetailsList = generalSlotDetailsRepository.findByStandardIdAndExamIdNullOrderByStart(standardId);
        return generalSlotDetailsMapper.toDto(generalSlotDetailsList);
    }




}
