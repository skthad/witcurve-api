package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.domain.StudentStandard;
import com.witcurve.repository.StudentStandardRepository;
import com.witcurve.service.EventService;
import com.witcurve.service.GeneralSlotDetailsService;
import com.witcurve.service.SlotCourseDetailsService;
import com.witcurve.service.dto.WeekViewDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

import static com.witcurve.service.util.WitcurveUtil.getLocalDate;

@RestController
@RequestMapping("/api")
public class WeekViewResource {

    private final Logger log = LoggerFactory.getLogger(WeekViewResource.class);

    @Autowired
    private StudentStandardRepository studentStandardRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private GeneralSlotDetailsService generalSlotDetailsService;

    @Autowired
    private SlotCourseDetailsService slotCourseDetailsService;


    /**
     *
     * @param weekDate
     * @param studentId
     * @return
     */
    @GetMapping("/week-view/student/{studentId}")
    @Timed
    public ResponseEntity<WeekViewDTO> getAllEventsForStudent(
        @RequestParam(value = "weekDate", required = false) String weekDate,
        @RequestParam(value = "year", required = false) Integer year,
        @RequestParam(value = "examId", required = false) Integer examId,
        @PathVariable Long studentId) throws WitcurveException, URISyntaxException {
        WeekViewDTO result = new WeekViewDTO();
        if(examId == null && weekDate !=null && year!=null) {
            log.debug("Request to get week views for student with id : {} with week day : {} and year : {}", studentId, weekDate, year);

        } else if((examId != null && weekDate ==null && year==null)){
            log.debug("Request to get exam week views for student with id : {} for exam with id : {}}", studentId, examId);
        } else {
            //throw error
        }
        StudentStandard studentStandard = studentStandardRepository.findByStudentIdAndActiveTrue(studentId);
        Long standardId = studentStandard.getStandard().getId();
        result.setGsdList(generalSlotDetailsService.getGeneralSlotDetailsByStandardId(standardId));
        result.setScdList(slotCourseDetailsService.getSlotCourseDetailsByStandardId(standardId));
        result.setEventList(eventService.findAllEventsOnGivenWeekForStudent(getLocalDate(weekDate), year, studentId));
        return new ResponseEntity<>(result,  HttpStatus.OK);
    }
}
