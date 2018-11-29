package com.witcurve.service.impl;

import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.domain.enumeration.GSDStatus;
import com.witcurve.repository.GeneralSlotDetailsRepository;
import com.witcurve.repository.StandardRepository;
import com.witcurve.service.GeneralSlotDetailsService;
import com.witcurve.service.dto.GeneralSlotDetailsDTO;
import com.witcurve.service.mapper.GeneralSlotDetailsMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


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
    public List<GeneralSlotDetailsDTO> create(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) {
        log.debug("Request to create generalSlotDetails");
        Map<Long, String> standardIdBindingValueMap = new HashMap<>();
        for (GeneralSlotDetailsDTO gsd : generalSlotDetailsDTOs) {
            Long standardId = gsd.getStandard().getId();
            if (standardIdBindingValueMap.get(standardId) == null) {
                String bindingId = UUID.randomUUID().toString();
                standardIdBindingValueMap.put(standardId, bindingId);
            }
            gsd.setBindingId(standardIdBindingValueMap.get(standardId));
        }
        generalSlotDetailsRepository.deactivateSlotDetailsForStandards(standardIdBindingValueMap.keySet());

        List<GeneralSlotDetails> generalSlotDetails = generalSlotDetailsMapper.toEntity(generalSlotDetailsDTOs);
        List<GeneralSlotDetailsDTO> slots = generalSlotDetailsMapper.toDto(generalSlotDetailsRepository.saveAll(generalSlotDetails));
        return slots;
    }

    @Override
    public List<GeneralSlotDetailsDTO> update(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) {
        log.debug("Request to update generalSlotDetails");
        List<GeneralSlotDetails> generalSlotDetails = generalSlotDetailsMapper.toEntity(generalSlotDetailsDTOs);
        List<GeneralSlotDetailsDTO> slots = generalSlotDetailsMapper.toDto(generalSlotDetailsRepository.saveAll(generalSlotDetails));
        return slots;
    }

    @Override
    public void activate(Long standardId, String bindingId) {

        generalSlotDetailsRepository.deactivateSlotDetailsForStandards(new HashSet<>(Arrays.asList(standardId)));
        generalSlotDetailsRepository.activateSlotDetailsForStandard(standardId, bindingId);
    }

    @Override
    public void deactivate(List<Long> standardIds) {
        generalSlotDetailsRepository.deactivateSlotDetailsForStandards(new HashSet<>(standardIds));
    }

    @Override
    public void clone(Long sourceStandardId, List<Long> destinationStandardIds) {
        log.debug("Request to clone generalSlotDetails ");
        List<GeneralSlotDetails> existingSlots = generalSlotDetailsRepository.findByStandardIdAndStatus(sourceStandardId, GSDStatus.ACTIVE);

            generalSlotDetailsRepository.deactivateSlotDetailsForStandards(new HashSet<>(destinationStandardIds));
        List<GeneralSlotDetailsDTO> slotsToCreate = new ArrayList<>();
        for (Long destinationStandardId : destinationStandardIds) {
            String bindingId = UUID.randomUUID().toString();
            for (GeneralSlotDetailsDTO slot : generalSlotDetailsMapper.toDto(existingSlots)) {
                slot.setId(null);
                slot.getStandard().setId(destinationStandardId);
                slot.setBindingId(bindingId);
                slotsToCreate.add(slot);
            }
        }
        generalSlotDetailsRepository.saveAll(generalSlotDetailsMapper.toEntity(slotsToCreate));

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
    public List<GeneralSlotDetailsDTO> getGeneralSlotDetailsByStandardIdAndStatus(Long standardId, GSDStatus status) throws WitcurveException {
        log.debug("Request to get generalSlotDetails by standard id : {}", standardId);
        List<GeneralSlotDetails> gsdList = generalSlotDetailsRepository.findByStandardIdAndStatus(standardId, status);

        return generalSlotDetailsMapper.toDto(gsdList);
    }

    @Override
    public void deleteByBindingId(String bindingId) throws WitcurveException {
        generalSlotDetailsRepository.deleteByBindingId(bindingId);
    }


}
