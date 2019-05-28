package com.witcurve.service;

import com.witcurve.domain.Event;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ViewType;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.dto.PeriodicTestDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    List<EventDTO> saveOrUpdate(List<EventDTO> eventDTOs) throws WitcurveException;

    EventDTO getEventById(Long eventId) throws WitcurveException;

    void deleteEvent(Long eventId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenDateForStudent(LocalDate eventDate, Long studentId) throws WitcurveException;

    List<EventDTO> findAllDiaryEventsForStudent(LocalDate eventDate, Long studentId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenDateForStaff(LocalDate eventDate, Long staffId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException;

    List<LocalDate> findAllEventDatesOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException;

    List<EventDTO> findAllEventsOnGivenMonthForStaff(Integer month, Integer year, Long staffId) throws WitcurveException;

    List<EventDTO> findUpcomingEventsForStudentsInWeek(LocalDate date, Long staffId) throws WitcurveException;

    List<EventDTO> findHolidaysOrSchoolEventsBySchoolInfoId(LocalDate fromDate, LocalDate endDate, Long schoolInfoId) throws WitcurveException;

    List<EventDTO> findUpcomingEventsForStaffInWeek(LocalDate date, Long staffId) throws WitcurveException;

    List<EventDTO> getAttendance(LocalDate fromDate, LocalDate toDate, Long studentId,
                                 Long standardId, Long staffId, Long schoolInfoId) throws WitcurveException;

    Page<EventDTO> getNotices(LocalDate startDate, LocalDate endDate, Long userId, List<Long> standardIds, List<String> keywords,Long schoolInfoId,Pageable pageable) throws WitcurveException;

    Page<PeriodicTestDTO> getPeriodTestsBetweenDates(Pageable pageable, LocalDate startDate, LocalDate endDate, Long schoolInfoId, List<Grade> grades) throws WitcurveException;

    List<EventDTO> getPeriodicTestsByBindingId(String bindingId, List<Long> courseIds);

    void deletePeriodicTestsByBindingId(String bindingId) throws WitcurveException;

    List<EventDTO> findAllTestAndAssignmentByTeacherInDateRange(Long staffId, LocalDate eventStart, LocalDate eventEnd, ViewType type) throws WitcurveException;

    Page<EventDTO> findAllTestAndAssignmentAndDailyUpdateByStandardAndCourse(Pageable pageable, LocalDate eventStart, LocalDate eventEnd, ViewType type, Long standardId, Long courseId) throws WitcurveException;

    List<EventDTO> findHolidaysInSchoolInfo(Long schoolInfoId, LocalDate startDate, LocalDate endDate) throws WitcurveException;

}
