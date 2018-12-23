package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.LeaveApplyor;
import com.witcurve.repository.*;
import com.witcurve.service.EventService;
import com.witcurve.service.LeaveApplicationService;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.service.mapper.EventMapper;
import com.witcurve.service.mapper.LeaveApplicationMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.hibernate.query.criteria.internal.expression.function.CurrentDateFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    private final Logger log  = LoggerFactory.getLogger(LeaveApplicationService.class);

    @Autowired
    LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    LeaveApplicationMapper leaveApplicationMapper;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    StandardRepository standardRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventService eventService;

    @Autowired
    EventMapper eventMapper;

    @Override
    public LeaveApplicationDTO saveOrUpdate(LeaveApplicationDTO leaveApplicationDTO, Boolean update) throws WitcurveException {
        log.debug("Request to save or update leave applications : {}", leaveApplicationDTO.toString());

        isLeaveApplicationValid(leaveApplicationDTO, update);
        Set<Event> events = new HashSet<>();
        LeaveApplication leaveApplication = leaveApplicationMapper.toEntity(leaveApplicationDTO);
        LocalDate localDate = LocalDate.now();
        LocalDate date1= localDate;
        if(localDate.isAfter(leaveApplication.getFromLeaveDate()) && localDate.isBefore(leaveApplication.getToLeaveDate()) ||
            (localDate.isAfter(leaveApplication.getToLeaveDate()))) {
            if(localDate.isAfter(leaveApplication.getToLeaveDate())){
                date1=leaveApplication.getToLeaveDate();
            }
            for (LocalDate date=leaveApplication.getFromLeaveDate();date.isBefore((date1).plusDays(1));date=date.plusDays(1)) {
                if (isHoliday(date, leaveApplication.getSession().getId())==false && date.getDayOfWeek() != DayOfWeek.SUNDAY && date.getDayOfWeek() != DayOfWeek.SATURDAY) {
                        Event e = eventRepository.findEventForStudent(date, leaveApplicationDTO.getAppliedStudentId());
                    //e.setPresent(false);
                    if(e != null) {
                        e.setName("Leave - "+leaveApplication.getReason());
                        e.setDescription(leaveApplication.getDescription());
                        e = eventRepository.save(e);
                        events.add(e);
                    }

                }
            }
        }
            leaveApplication.setEvents(events);
            leaveApplication = leaveApplicationRepository.save(leaveApplication);
        return  leaveApplicationMapper.toDto(leaveApplication);
        }

    @Override
    public LeaveApplicationDTO getLeaveApplicationById(Long leaveApplicationId) throws WitcurveException {
        log.debug("Request to get leave application with id : {}", leaveApplicationId);

        Optional<LeaveApplication> leaveApplication = leaveApplicationRepository.findById(leaveApplicationId);

        if (!leaveApplication.isPresent()) {
            throw new WitcurveException("No leave application with given id");
        }
        return leaveApplicationMapper.toDto(leaveApplication.get());
    }

    @Override
    public void deleteLeaveApplication(Long leaveApplicationId) throws WitcurveException {
        log.debug("Request to delete leave Application with id {}", leaveApplicationId);
        Optional<LeaveApplication> leaveApplication = leaveApplicationRepository.findById(leaveApplicationId);
        if (!leaveApplication.isPresent()){
            throw new WitcurveException("No leave application with given id");
        }
        leaveApplicationRepository.delete(leaveApplication.get());

    }

    @Override
    public LeaveApplicationDTO getLeaveApplicationApprover(Long leaveApplicationId,Long staffId) throws WitcurveException {
        log.debug("Approval for leaveApplication with id {}, by staff id {}",leaveApplicationId, staffId);
        Optional<LeaveApplication> leaveApplication = leaveApplicationRepository.findById(leaveApplicationId);
        Optional<Staff> staff=  staffRepository.findById(staffId);
        if (!leaveApplication.isPresent()) {
            throw new WitcurveException("No leave application with given id");
        }
        if (!staff.isPresent()) {
            throw new WitcurveException("No Staff with given id");
        }
        LeaveApplication toBeApprovedLeave = leaveApplication.get();
        toBeApprovedLeave.setApproved(true);
        toBeApprovedLeave.setApprovedBy(staff.get());
        toBeApprovedLeave = leaveApplicationRepository.save(toBeApprovedLeave);

        return leaveApplicationMapper.toDto(toBeApprovedLeave);
    }

    @Override
    public List<LeaveApplicationDTO> getLeaveApplicationsForStudent(Long studentId, Long sessionId, Boolean approved) {
        log.debug("Get list of leaveApplication with  student id : {} and session id : {}",sessionId);
        List<LeaveApplication> leaveApplications = new ArrayList<>();
        if(approved == null) {
            leaveApplications = leaveApplicationRepository.findBySessionIdAndAppliedStudentIdOrderByCreatedDate(sessionId,studentId);
        } else {
            if(approved) {
                leaveApplications = leaveApplicationRepository.findBySessionIdAndAppliedStudentIdAndApprovedTrueOrderByCreatedDate(sessionId, studentId);
            } else {
                leaveApplications = leaveApplicationRepository.findBySessionIdAndAppliedStudentIdAndApprovedFalseOrderByCreatedDate(sessionId, studentId);
            }
        }

        return leaveApplicationMapper.toDto(leaveApplications);
    }

    @Override
    public List<LeaveApplicationDTO> getLeaveApplicationsForStaff(Long staffId, Long sessionId, Boolean approved) {
        log.debug("Get list of leaveApplication with staff id {} and session id {}", staffId, sessionId);
        List<LeaveApplication> leaveApplications= new ArrayList<>();
        if(approved == null) {
            leaveApplications = leaveApplicationRepository.findBySessionIdAndAppliedStaffIdOrderByFromLeaveDate(sessionId,staffId);
        } else {
            if(approved) {
                leaveApplications = leaveApplicationRepository.findBySessionIdAndAppliedStaffIdAndApprovedTrueOrderByFromLeaveDate(sessionId, staffId);
            } else {
                leaveApplications = leaveApplicationRepository.findBySessionIdAndAppliedStaffIdAndApprovedFalseOrderByFromLeaveDate(sessionId, staffId);
            }
        }
        return leaveApplicationMapper.toDto(leaveApplications);
    }

    public List<LeaveApplicationDTO> getLeaveApplicationsForStandard(Long standardId, Boolean approved) throws WitcurveException {
        Optional<Standard> standard = standardRepository.findById(standardId);
        if(!standard.isPresent()) {
            throw new WitcurveException("No standard exists with given id");
        }
        Long sessionId = standard.get().getTerm().getSession().getId();
        List<LeaveApplication> leaveApplications = new ArrayList<>();
        List<Long> studentIds = studentStandardRepository.findStudentIdByStandardId(standardId);
        if(studentIds.size() !=0) {
            if(approved == null) {
                leaveApplications = leaveApplicationRepository.findLeaveAppicationsForStudentsInASession(sessionId, studentIds);
            } else {
                if(approved) {
                    leaveApplications = leaveApplicationRepository.findApprovedLeaveApplicationsForStudentsInASession(sessionId, studentIds);
                } else {
                    leaveApplications = leaveApplicationRepository.findUnApprovedLeaveApplicationsForStudentsInASession(sessionId, studentIds);
                }
            }
        }

        return leaveApplicationMapper.toDto(leaveApplications);
    }

    private Boolean isHoliday(LocalDate date, Long sessionId) throws WitcurveException {

        if(eventRepository.findHolidaysBetweenFromDateAndToDate(date,date, sessionId)==1)
            return true;
        else
            return false;
    }

    public Long workingDays(LocalDate fromDate, LocalDate toDate, Long sessionId, Boolean isSaturdayWorking)
        throws WitcurveException {
        Optional<AcademicSession> academicSession = academicSessionRepository.findById(sessionId);
        if(!academicSession.isPresent()) {
            throw new WitcurveException("session id not present !");
        }
        LocalDate startDate= academicSession.get().getStartDate();
        LocalDate endDate = startDate.plusYears(1);
        Long workingDays = 0L;
        if (fromDate.isAfter(toDate)) {
            throw new WitcurveException("from date cannot be after to date.");
        }
        if (fromDate.isAfter(startDate) && toDate.isBefore(endDate)) {
            if (isSaturdayWorking == false) {
                for(LocalDate date=fromDate ; date.isBefore(toDate) || date.equals(toDate); date= date.plusDays(1)){
                    if (date.getDayOfWeek() != DayOfWeek.SUNDAY && date.getDayOfWeek() != DayOfWeek.SATURDAY) {
                        workingDays++;
                    }
                }
            } else {
                for(LocalDate date=fromDate ; date.isBefore(toDate) || date.equals(toDate) ; date=date.plusDays(1)){
                    if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                        workingDays++;
                    }
                }
            }
            // to remove the holidays
            Long holidays = eventRepository.findHolidaysBetweenFromDateAndToDate(fromDate,toDate, sessionId);
            workingDays = workingDays - holidays;
        }
        else
        {
            throw new WitcurveException("start date or end date are out of academic session");
        }
        return workingDays;
    }

    private Event createEvent(LeaveApplicationDTO leaveApplicationDTO,LocalDate date){
        //List<EventDTO> eventDTO = new ArrayList<>();
        LeaveApplication leaveApplications = leaveApplicationMapper.toEntity(leaveApplicationDTO);
        Event event = new Event();
        event.setName("Leave - "+leaveApplications.getReason());
        event.setType(EventType.ATTENDANCE);
        event.setPresent(false);
        if(leaveApplicationDTO.getType().equals(LeaveApplyor.STUDENT)) {
        event.setStudent(leaveApplications.getAppliedStudent());}
        else if(leaveApplicationDTO.getType().equals(LeaveApplyor.STAFF)){
        event.setStaff(leaveApplications.getAppliedStaff()); }
        event.setAcademicSession(leaveApplications.getSession());
        event.setDate(date);
        //eventDTO.add(eventMapper.toDto(event));
        //eventService.saveOrUpdate(eventDTO);
        eventRepository.save(event);
        return event;
    }

    private void isLeaveApplicationValid(LeaveApplicationDTO leaveApplicationDTO, Boolean update) throws WitcurveException {
        log.debug("Request to check valid leaveApplications in list : {}",leaveApplicationDTO);
            if (leaveApplicationDTO.getType().equals(LeaveApplyor.STUDENT)) {
                if (leaveApplicationDTO.getAppliedStudentId() == null || leaveApplicationDTO.getAppliedGuardianId() == null) {
                    log.error("There either applied student id or applied guardian id is null for student leave application : {}", leaveApplicationDTO);
                    throw new WitcurveException("Invalid Request Body");
                }
                Long workingDays = workingDays(leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate(),leaveApplicationDTO.getSessionId(),false);
                if(workingDays==0){
                    throw new WitcurveException("total working days is 0 so leave application cannot be created !");
                }
                List<LeaveApplication> leaveApplications = leaveApplicationRepository.findLeaveApplicationsForStudentInADateRange(leaveApplicationDTO.getSessionId() ,leaveApplicationDTO.getAppliedStudentId(),leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate());
                if(update) {
                    if (leaveApplications.size() > 1) {
                        throw new WitcurveException("The Leave application for this student already exists in the date range !! ");
                    } else if (leaveApplications.size() == 1) {
                        if (!leaveApplicationDTO.getId().equals(leaveApplications.get(0).getId())) {
                            throw new WitcurveException("The Leave application for this student  already exists in the date range !! ");
                        }
                    } else {
                        throw new WitcurveException("Leave application doesn't exist with give id");
                    }
                } else {
                    if(leaveApplications.size() != 0) {
                        throw new WitcurveException("The Leave application for this staff already exists in the date range !! ");
                    }
                }
            }
            if (leaveApplicationDTO.getType().equals(LeaveApplyor.STAFF)) {
                if (leaveApplicationDTO.getAppliedStaffId() == null) {
                    log.error("Staff id is null for student leave application : {}", leaveApplicationDTO);
                    throw new WitcurveException("Invalid Request Body");
                }
                Long workingDays = workingDays(leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate(),leaveApplicationDTO.getSessionId(),false);
                if(workingDays==0){
                    throw new WitcurveException("total working days is 0 so leave application cannot be created !");
                }
                List<LeaveApplication> leaveApplications = leaveApplicationRepository.findLeaveApplicationsForStaffInADateRange(leaveApplicationDTO.getSessionId() ,leaveApplicationDTO.getAppliedStaffId(),leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate());
                if(update) {
                    if(leaveApplications.size() >1) {
                        throw new WitcurveException("The Leave application for this staff already exists in the date range !! ");
                    } else if(leaveApplications.size() == 1){
                        if(!leaveApplicationDTO.getId().equals(leaveApplications.get(0).getId())) {
                            throw new WitcurveException("The Leave application for this staff already exists in the date range !! ");
                        }
                    } else {
                        throw new WitcurveException("Leave application doesn't exist with give id");
                    }
                } else {
                    if(leaveApplications.size() != 0) {
                        throw new WitcurveException("The Leave application for this staff already exists in the date range !! ");
                    }
                }
            }
    }
}
