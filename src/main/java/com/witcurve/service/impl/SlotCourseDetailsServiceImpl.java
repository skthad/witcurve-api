package com.witcurve.service.impl;

import com.witcurve.domain.SlotCourseDetails;
import com.witcurve.repository.SlotCourseDetailsRepository;
import com.witcurve.service.SlotCourseDetailsService;
import com.witcurve.service.dto.SlotCourseDetailsDTO;
import com.witcurve.service.mapper.SlotCourseDetailsMapper;
import com.witcurve.service.mapper.SlotCourseDetailsMapperLite;
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
    private SlotCourseDetailsMapperLite slotCourseDetailsMapperLite;

    @Autowired
    private SlotCourseDetailsRepository slotCourseDetailsRepository;

    @Override
    public List<SlotCourseDetailsDTO> saveOrUpdate(List<SlotCourseDetailsDTO> slotCourseDetailsDTOs) {
        log.debug("Request to save or update slotCourseDetails");
        List<SlotCourseDetails> slotCourseDetails = slotCourseDetailsMapperLite.toEntity(slotCourseDetailsDTOs);
        slotCourseDetails = slotCourseDetailsRepository.saveAll(slotCourseDetails);
        return slotCourseDetailsMapperLite.toDto(slotCourseDetails);
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
    public List<SlotCourseDetailsDTO> getSlotCourseDetailsByStandardId(Long standardId) {
        log.debug("Requqest to get list of slotCourseDetails for given standard id : {}", standardId);
        List<SlotCourseDetails> slotCourseDetailsList = slotCourseDetailsRepository.findByStandardIdOrderByGsdStart(standardId);
        return slotCourseDetailsMapper.toDto(slotCourseDetailsList);
    }

    @Override
    public List<SlotCourseDetailsDTO> getSlotCourseDetailsByTeacherIdAndTermId(Long teacherId, Long termId) {
        log.debug("Requqest to get list of slotCourseDetails for given teacher id  and term id: {}", teacherId, termId);
        List<SlotCourseDetails> slotCourseDetailsList = slotCourseDetailsRepository.findByTeacherIdAndTermIdOrderByGsdStart(teacherId, termId);
        return slotCourseDetailsMapper.toDto(slotCourseDetailsList);
    }

}
