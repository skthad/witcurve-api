package com.witcurve.service;

import com.witcurve.domain.enumeration.ViewType;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    List<EventDTO> saveOrUpdate(List<EventDTO> eventDTOs) throws WitcurveException;

    EventDTO getEventById(Long eventId) throws WitcurveException;

    void deleteEvent(Long eventId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenDateForStudent(LocalDate eventDate, Long studentId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenDateForStaff(LocalDate eventDate, Long staffId, Long termId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException;

    List<LocalDate> findAllEventDatesOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenWeekForStudent(LocalDate weekDate, Integer year, Long studentId) throws WitcurveException;

    List<EventDTO> findAllEventsForDiary(LocalDate date, Long studentId) throws WitcurveException;

    List<EventDTO> findEventsByDateRangeForStudentInUpcomingEvents(LocalDate date, Long staffId) throws WitcurveException;

    List<EventDTO> findEventsByDateRangeForStaffInUpcomingEvents(LocalDate date, Long staffId) throws WitcurveException;

    List<EventDTO> getAllLeavesForStudent(LocalDate date, Long studentId) throws WitcurveException;

    List<EventDTO> getAllLeavesForStandard(LocalDate date, Long standardId) throws WitcurveException;

    List<EventDTO> getAttendance(LocalDate fromDate, LocalDate toDate, Long studentId, Long standardId, Long staffId) throws WitcurveException;

    Page<EventDTO> getNotices(Long termId, Long studentId, Long staffId, Pageable pageable) throws WitcurveException;

    List<EventDTO> findAllTestAndAssignmentByTeacherInWeek(Long staffId, Long termId, LocalDate date, ViewType type) throws WitcurveException;
}
