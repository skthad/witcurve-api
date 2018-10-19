package com.witcurve.service;

import com.witcurve.service.dto.EventDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    List<EventDTO> saveOrUpdate(List<EventDTO> eventDTOs) throws WitcurveException;

    EventDTO getEventById(Long eventId) throws WitcurveException;

    void deleteEvent(Long eventId) throws WitcurveException;

    //List<EventDTO> findAllEventsOnGivenDateForStandard(LocalDate eventDate, Long standardId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenDateForStudent(LocalDate eventDate, Long studentId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException;

    List<LocalDate> findAllEventDatesOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenWeekForStudent(LocalDate weekDate, Integer year, Long studentId) throws WitcurveException;

    List<EventDTO> findAllEventsForDiary(LocalDate date, Long studentId) throws WitcurveException;

    List<EventDTO> findAllEventsForAnnouncements(LocalDate date, Long studentId) throws WitcurveException;

    List<EventDTO> getAllLeavesForStudent(LocalDate date, Long studentId) throws WitcurveException;

    List<EventDTO> getAllLeavesForStandard(LocalDate date, Long standardId) throws WitcurveException;
}
