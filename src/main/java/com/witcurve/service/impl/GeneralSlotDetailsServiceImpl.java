package com.witcurve.service.impl;

import com.witcurve.domain.Class;
import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.repository.ClassRepository;
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
    private ClassRepository classRepository;

    @Override
    public List<GeneralSlotDetailsDTO> saveOrUpdate(List<GeneralSlotDetailsDTO> generalSlotDetailsDTOs) {
        log.debug("Request to save or update generalSlotDetails");
        List<GeneralSlotDetails> generalSlotDetails = generalSlotDetailsMapper.toEntity(generalSlotDetailsDTOs);
        List<GeneralSlotDetailsDTO> slots = generalSlotDetailsMapper.toDto(generalSlotDetailsRepository.saveAll(generalSlotDetails));
        return slots;
    }

    @Override
    public void clone(Long sourceClassId, List<Long> destinationClassIds) {
        log.debug("Request to clone generalSlotDetails ");
        List<GeneralSlotDetails> slots = generalSlotDetailsRepository.findByStandardIdAndExamIdNullOrderByStart(sourceClassId);

        for (Long destinationClassId : destinationClassIds) {
            List<GeneralSlotDetails> slotsToCreate = slots.stream().collect(Collectors.toList());
            Class standard = classRepository.findById(destinationClassId).get();
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
    public List<GeneralSlotDetailsDTO> getGeneralSlotDetailsByClassId(Long classId) throws WitcurveException {
        log.debug("Request to get generalSlotDetails by class id : {}", classId);
        List<GeneralSlotDetails> generalSlotDetailsList = generalSlotDetailsRepository.findByStandardIdAndExamIdNullOrderByStart(classId);
        return generalSlotDetailsMapper.toDto(generalSlotDetailsList);
    }




}
