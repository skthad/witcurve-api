package com.witcurve.service.impl;

import com.witcurve.domain.CourseTeacher;
import com.witcurve.domain.GeneralSlotDetails;
import com.witcurve.domain.SlotCourseDetails;
import com.witcurve.repository.CourseTeacherRepository;
import com.witcurve.repository.EventRepository;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private EventRepository eventRepository;

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

        List<SlotCourseDetails> slotCourseDetailsList = slotCourseDetailsRepository.findActiveScdByGsdAndDayOfWeek(slotCourseDetailsDTO.getGsd().getId(), slotCourseDetailsDTO.getDayOfWeek());

        //not allowing non delete scd to have gsd and week of day restriction.
        if(slotCourseDetailsList.size() > 1) {
            throw new WitcurveException("There already exists an active slot within giving timings for this day of the week to a certain course");
        } else if(slotCourseDetailsList.size() ==1) {
            if(slotCourseDetailsDTO.getId() != null) {
                if(!slotCourseDetailsDTO.getId().equals(slotCourseDetailsList.get(0).getId())) {
                    throw new WitcurveException("There already exists an active slot within giving timings for this day of the week to a certain course");
                }
            } else {
                throw new WitcurveException("There already exists an active slot within giving timings for this day of the week to a certain course");
            }
        } else {
           if(slotCourseDetailsDTO.getId() != null) {
               throw new WitcurveException("There already exists an active slot within giving timings for this day of the week to a certain course");
           }
        }


        Integer startTime = Integer.parseInt(gsd.get().getStart());
        Integer endTime = startTime + gsd.get().getDuration();
        Long teacherId = ct.get().getTeacher().getId();
        Long schoolInfoId = ct.get().getTeacher().getSchoolInfo().getId();

        List<Long> allocatedTeachers = slotCourseDetailsRepository
            .findAllocatedTeachersList(startTime, endTime, slotCourseDetailsDTO.getDayOfWeek(), schoolInfoId);
        if (allocatedTeachers.indexOf(teacherId) > -1) {
            throw new WitcurveException("This teacher is already allocated during this slot window");
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
    public void deleteSlotCourseDetails(Long slotCourseDetailsId, Boolean softDelete) throws WitcurveException {
        log.debug("Request to delete slotCourseDetails by id : {}", slotCourseDetailsId);
        SlotCourseDetails slotCourseDetails = slotCourseDetailsRepository.findById(slotCourseDetailsId).get();
        if(slotCourseDetails == null) {
            throw new WitcurveException("No SlotCourseDetails exists for given id");
        }
        if (softDelete) {
            slotCourseDetails.setDeleted(true);
            eventRepository.deleteFutureTestByScdIds(Stream.of(slotCourseDetailsId).collect(Collectors.toSet()));
            slotCourseDetailsRepository.save(slotCourseDetails);
        } else {
            slotCourseDetailsRepository.delete(slotCourseDetails);
        }
    }

    @Override
    public List<SlotCourseDetailsDTO> getSlotCourseDetailsByStandardId(Long standardId) {
        log.debug("Requqest to get list of slotCourseDetails for given standard id : {}", standardId);
        List<SlotCourseDetails> slotCourseDetailsList = slotCourseDetailsRepository.findByStandardIdOrderByGsdStartTime(standardId);
        return slotCourseDetailsMapper.toDto(slotCourseDetailsList);
    }

    @Override
    public List<SlotCourseDetailsDTO> getSlotCourseDetailsByTeacherId(Long teacherId) {
        log.debug("Requqest to get list of slotCourseDetails for given teacher id  and term id: {}", teacherId);
        List<SlotCourseDetails> slotCourseDetailsList = slotCourseDetailsRepository.findByTeacherIdOrderByGsdStartTime(teacherId);
        return slotCourseDetailsMapper.toDto(slotCourseDetailsList);
    }

}
