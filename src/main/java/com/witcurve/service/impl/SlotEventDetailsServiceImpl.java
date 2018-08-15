package com.witcurve.service.impl;

import com.witcurve.domain.SlotEventDetails;
import com.witcurve.repository.SlotEventDetailsRepository;
import com.witcurve.service.SlotEventDetailsService;
import com.witcurve.service.dto.SlotEventDetailsDTO;
import com.witcurve.service.mapper.SlotEventDetailsMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SlotEventDetailsServiceImpl implements SlotEventDetailsService {

    private final Logger log  = LoggerFactory.getLogger(SlotEventDetailsServiceImpl.class);

    @Autowired
    private SlotEventDetailsMapper slotEventDetailsMapper;

    @Autowired
    private SlotEventDetailsRepository slotEventDetailsRepository;

    public SlotEventDetailsDTO saveOrUpdate(SlotEventDetailsDTO slotEventDetailsDTO) {
        log.debug("Request to save or update slotEventDetails : {}", slotEventDetailsDTO);
        SlotEventDetails slotEventDetails = slotEventDetailsMapper.toEntity(slotEventDetailsDTO);
        slotEventDetails = slotEventDetailsRepository.save(slotEventDetails);
        return slotEventDetailsMapper.toDto(slotEventDetails);
    }

    public SlotEventDetailsDTO getSlotEventDetailsById(Long slotEventDetailsId) throws WitcurveException {
        log.debug("Request to get slotEventDetails by id : {}", slotEventDetailsId);
        SlotEventDetails slotEventDetails = slotEventDetailsRepository.findById(slotEventDetailsId).get();
        if(slotEventDetails == null) {
            throw new WitcurveException("No SlotEventDetails exists for given id");
        }
        return slotEventDetailsMapper.toDto(slotEventDetails);
    }

    public void deleteSlotEventDetailsById(Long slotEventDetailsId) throws WitcurveException {
        log.debug("Request to get slotEventDetails by id : {}", slotEventDetailsId);
        SlotEventDetails slotEventDetails = slotEventDetailsRepository.findById(slotEventDetailsId).get();
        if(slotEventDetails == null) {
            throw new WitcurveException("No SlotEventDetails exists for given id");
        }
        slotEventDetailsRepository.deleteById(slotEventDetailsId);
    }

}
