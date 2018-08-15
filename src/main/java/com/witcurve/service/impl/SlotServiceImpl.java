package com.witcurve.service.impl;

import com.witcurve.domain.Slot;
import com.witcurve.repository.SlotRepository;
import com.witcurve.service.SlotService;
import com.witcurve.service.dto.SlotDTO;
import com.witcurve.service.mapper.SlotMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SlotServiceImpl implements SlotService {

    private final Logger log  = LoggerFactory.getLogger(SlotServiceImpl.class);

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    SlotMapper slotMapper;


    @Override
    public SlotDTO saveOrUpdate(SlotDTO slotDTO) {
        log.debug("Request to save or update slot : {}", slotDTO);
        Slot slot = slotMapper.toEntity(slotDTO);
        slot = slotRepository.save(slot);
        return slotMapper.toDto(slot);
    }

    @Override
    public SlotDTO getSlotById(Long slotId) throws WitcurveException {
        log.debug("Request to get slot with id : {}", slotId);
        Slot slot = slotRepository.findById(slotId).get();

        if (slot == null) {
            throw new WitcurveException("No Slot with given id");
        }
        return slotMapper.toDto(slot);
    }

    @Override
    public void deleteSlotById(Long slotId) throws WitcurveException {
        log.debug("Request to delete slot with id : {}", slotId);
        Slot slot = slotRepository.findById(slotId).get();

        if (slot == null) {
            throw new WitcurveException("No Slot with given id");
        }
        slotRepository.delete(slot);
    }
}
