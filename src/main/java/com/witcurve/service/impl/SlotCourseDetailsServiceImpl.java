package com.witcurve.service.impl;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.domain.SlotCourseDetails;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.repository.GeneralSlotDetailsRepository;
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
import java.util.Optional;

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

    @Autowired
    private GeneralSlotDetailsRepository generalSlotDetailsRepository;

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    @Override
    public SlotCourseDetailsDTO saveOrUpdate(SlotCourseDetailsDTO slotCourseDetailsDTO) throws WitcurveException {
        log.debug("Request to save or update slotCourseDetails");

        //TODO check the logic that the teacher is avialble in the time period
        Optional<GeneralSlotDetails> gsd = generalSlotDetailsRepository.findById(slotCourseDetailsDTO.getGsd().getId());
        if (!gsd.isPresent()) {
            throw new WitcurveException("Could not find GSD with id: " + slotCourseDetailsDTO.getGsd().getId());
        }

        Optional<CourseTeacher> ct = courseTeacherRepository.findById(slotCourseDetailsDTO.getCourseTeacher().getId());
        if (!ct.isPresent()) {
            throw new WitcurveException("Could not find CourseTeacher with id: " + slotCourseDetailsDTO.getCourseTeacher().getId());
        }

        Integer startTime = gsd.get().getStartTime();
        Integer endTime = startTime + gsd.get().getDuration();
        Long teacherId = ct.get().getTeacher().getId();
        Long schoolInfoId = ct.get().getTeacher().getSchoolInfo().getId();

        List<Long> allocatedTeachers = slotCourseDetailsRepository
            .findAllocatedTechersList(startTime, endTime, slotCourseDetailsDTO.getDayOfWeek(), schoolInfoId);
        if (allocatedTeachers.indexOf(teacherId) > -1) {
            throw new WitcurveException("This teacher is already allocated to during this slot window");
        }
        SlotCourseDetails slotCourseDetails = slotCourseDetailsMapperLite.toEntity(slotCourseDetailsDTO);
        slotCourseDetails = slotCourseDetailsRepository.save(slotCourseDetails);
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
        List<SlotCourseDetails> slotCourseDetailsList = slotCourseDetailsRepository.findByStandardIdOrderByGsdStartTime(standardId);
        return slotCourseDetailsMapperLite.toDto(slotCourseDetailsList);
    }

    @Override
    public List<SlotCourseDetailsDTO> getSlotCourseDetailsByTeacherIdAndTermId(Long teacherId, Long termId) {
        log.debug("Requqest to get list of slotCourseDetails for given teacher id  and term id: {}", teacherId, termId);
        List<SlotCourseDetails> slotCourseDetailsList = slotCourseDetailsRepository.findByTeacherIdAndTermIdOrderByGsdStartTime(teacherId, termId);
        return slotCourseDetailsMapper.toDto(slotCourseDetailsList);
    }

}
