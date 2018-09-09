package com.witcurve.service.impl;

import afu.org.checkerframework.checker.oigj.qual.O;
import com.witcurve.domain.SlotCourseDetails;
import com.witcurve.repository.SlotCourseDetailsRepository;
import com.witcurve.service.SlotCourseDetailsService;
import com.witcurve.service.dto.SlotCourseDetailsDTO;
import com.witcurve.service.mapper.SlotCourseDetailsMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SlotCourseDetailsServiceImpl implements SlotCourseDetailsService {

    private final Logger log  = LoggerFactory.getLogger(SlotCourseDetailsServiceImpl.class);

    @Autowired
    private SlotCourseDetailsMapper slotCourseDetailsMapper;

    @Autowired
    private SlotCourseDetailsRepository slotCourseDetailsRepository;

    @Override
    public SlotCourseDetailsDTO saveOrUpdate(SlotCourseDetailsDTO slotCourseDetailsDTO) {
        log.debug("Request to save or update slotCourseDetails : {}", slotCourseDetailsDTO);
        SlotCourseDetails slotCourseDetails = slotCourseDetailsMapper.toEntity(slotCourseDetailsDTO);
        slotCourseDetails = slotCourseDetailsRepository.save(slotCourseDetails);
        return slotCourseDetailsMapper.toDto(slotCourseDetails);
    }

    @Override
    public SlotCourseDetailsDTO getSlotCourseDetailsById(Long slotCourseDetailsId) throws WitcurveException {
        log.debug("Request to get slotCourseDetails by id : {}", slotCourseDetailsId);
        SlotCourseDetails slotCourseDetails = slotCourseDetailsRepository.findById(slotCourseDetailsId).get();
        if(slotCourseDetails == null) {
            throw new WitcurveException("No SlotCourseDetails exists for given id");
        }
        return slotCourseDetailsMapper.toDto(slotCourseDetails);
    }

    @Override
    public void deleteSlotCourseDetails(Long slotCourseDetailsId) throws WitcurveException {
        log.debug("Request to delete slotCourseDetails by id : {}", slotCourseDetailsId);
        SlotCourseDetails slotCourseDetails = slotCourseDetailsRepository.findById(slotCourseDetailsId).get();
        if(slotCourseDetails == null) {
            throw new WitcurveException("No SlotCourseDetails exists for given id");
        }
        slotCourseDetailsRepository.delete(slotCourseDetails);
    }

    @Override
    public List<SlotCourseDetailsDTO> getSlotCourseDetailsByClassId(Long classId) {
        log.debug("Requqest to get list of slotCourseDetails for given class id : {}", classId);
        List<SlotCourseDetails> slotCourseDetailsList = slotCourseDetailsRepository.findByStandardIdOrderByClassId(classId);
        return slotCourseDetailsMapper.toDto(slotCourseDetailsList);
    }

}
