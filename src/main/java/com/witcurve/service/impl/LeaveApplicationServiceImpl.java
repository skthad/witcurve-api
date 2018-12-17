package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.LeaveApplyor;
import com.witcurve.repository.*;
import com.witcurve.service.LeaveApplicationService;
import com.witcurve.service.dto.LeaveApplicationDTO;
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


    @Override
    public LeaveApplicationDTO saveOrUpdate(LeaveApplicationDTO leaveApplicationDTO) throws WitcurveException {
        log.debug("Request to save or update leave applications : {}", leaveApplicationDTO.toString());

        isLeaveApplicationValid(leaveApplicationDTO);
        Set<Event> events = new HashSet<>();
        LeaveApplication leaveApplications = leaveApplicationMapper.toEntity(leaveApplicationDTO);
        LocalDate localDate = LocalDate.now();
        LocalDate date1= localDate;
        if(localDate.isAfter(leaveApplications.getFromLeaveDate()) && localDate.isBefore(leaveApplications.getToLeaveDate()) ||
            (localDate.isAfter(leaveApplications.getToLeaveDate()))) {
            if(localDate.isAfter(leaveApplications.getToLeaveDate())){
                date1=leaveApplications.getToLeaveDate();
            }
            for (LocalDate date=leaveApplications.getFromLeaveDate();date.isBefore((date1).plusDays(1));date=date.plusDays(1)) {
                if (isHoliday(date)==false && date.getDayOfWeek() != DayOfWeek.SUNDAY && date.getDayOfWeek() != DayOfWeek.SATURDAY) {
                    if (eventRepository.findLeaveForStudent(date, leaveApplicationDTO.getAppliedStudentId()).size() < 1) {
                        events.add(createEvent(leaveApplicationDTO, date));
                    }
                    else if (eventRepository.findLeaveForStaff(date, leaveApplicationDTO.getAppliedStaffId()).size() < 1) {
                        events.add(createEvent(leaveApplicationDTO, date));
                    }
                    else {
                        Event e = eventRepository.findEventForStudent(date, leaveApplicationDTO.getAppliedStudentId());
                        if(leaveApplicationDTO.getType().equals(LeaveApplyor.STAFF)) {
                            e = eventRepository.findEventForStudent(date, leaveApplicationDTO.getAppliedStaffId());
                            e.setStaff(leaveApplications.getAppliedStaff());
                        }
                        e.setPresent(false);
                        e.setName("Leave - "+leaveApplications.getReason());
                        e.setDescription(leaveApplications.getDescription());
                        eventRepository.save(e);
                        events.add(e);
                    }
                }
            }
        }
            leaveApplications.setEvents(events);
            leaveApplications = leaveApplicationRepository.save(leaveApplications);
        return  leaveApplicationMapper.toDto(leaveApplications);
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
        if(leaveApplication.get().getEvents().size()==0){
            ;
        }
        else if(leaveApplication.get().getEvents().size()>0) {
            toBeApprovedLeave.setApproved(true);
            toBeApprovedLeave.setApprovedBy(staff.get());
            toBeApprovedLeave = leaveApplicationRepository.save(toBeApprovedLeave);
        }

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

    public List<LeaveApplicationDTO>  getLeaveApplicationsForAStudent(Long studentId, Long sessionId) throws WitcurveException {
        Optional<Student> student = studentRepository.findById(studentId);
        if(!student.isPresent()) {
            throw new WitcurveException("No student exists with given id");
        }
        List<LeaveApplication> leaveApplications = new ArrayList<>();
        leaveApplications = leaveApplicationRepository.findLeaveAppicationsForStudentInASession(sessionId,studentId);
        if(leaveApplications.size()==0){
            throw new WitcurveException("The total leave applications is 0 for the student with id :"+studentId);
        }
        return leaveApplicationMapper.toDto(leaveApplications);
    }

    public List<LeaveApplicationDTO>  getLeaveApplicationsForAStaff(Long staffId, Long sessionId) throws WitcurveException {
        Optional<Student> staff = studentRepository.findById(staffId);
        if(!staff.isPresent()) {
            throw new WitcurveException("No student exists with given id");
        }
        List<LeaveApplication> leaveApplications = new ArrayList<>();
        leaveApplications = leaveApplicationRepository.findLeaveAppicationsForStaffInASession(sessionId,staffId);
        if(leaveApplications.size()==0){
            throw new WitcurveException("The total leave applications is 0 for the student with id :"+staffId);
        }
        return leaveApplicationMapper.toDto(leaveApplications);
    }

    public Long getLeaveCount(Long leaveApplicationId,Long sessionId, Boolean isSaturdayWorking) throws WitcurveException {
        log.debug("Request to count number of working days for leave Application with id {}", leaveApplicationId);
        Optional<LeaveApplication> leaveApplication = leaveApplicationRepository.findById(leaveApplicationId);
        Long workingDays= (Long) workingDays(leaveApplication.get().getFromLeaveDate(), leaveApplication.get().getToLeaveDate(),sessionId,isSaturdayWorking);
        return workingDays;
    }

    private Boolean isHoliday(LocalDate date) throws WitcurveException {

        if(eventRepository.findHolidaysBetweenFromDateAndToDate(date,date)==1)
            return true;
        else
            return false;
    }

    private Long workingDays(LocalDate fromDate, LocalDate toDate, Long sessionId, Boolean isSaturdayWorking)
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
             toDate= toDate.plusDays(1);
            if (isSaturdayWorking == false) {
                for(LocalDate date=fromDate ; date.isBefore(toDate); date= date.plusDays(1)){
                    if (date.getDayOfWeek() != DayOfWeek.SUNDAY && date.getDayOfWeek() != DayOfWeek.SATURDAY) {
                        workingDays++;
                    }
                }
            } else {
                for(LocalDate date=fromDate ; date.isBefore(toDate); fromDate.plusDays(1)){
                    if (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                        workingDays++;
                    }
                }
            }
            // to remove the holidays
            Long holidays = eventRepository.findHolidaysBetweenFromDateAndToDate(fromDate,toDate);
            workingDays = workingDays - holidays;
        }
        else
        {
            throw new WitcurveException("start date or end date are out of academic session");
        }
        if(workingDays==0){
            throw new WitcurveException("total working days is 0 so leave application cannot be created !");
        }
        return workingDays;
    }

    private Event createEvent(LeaveApplicationDTO leaveApplicationDTO,LocalDate date){

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
        eventRepository.save(event);
        return event;
    }

    private void isLeaveApplicationValid(LeaveApplicationDTO leaveApplicationDTO) throws WitcurveException {
        log.debug("Request to check valid leaveApplications in list : {}",leaveApplicationDTO);
            if (leaveApplicationDTO.getType().equals(LeaveApplyor.STUDENT)) {
                if (leaveApplicationDTO.getAppliedStudentId() == null || leaveApplicationDTO.getAppliedGuardianId() == null) {
                    log.error("There either applied student id or applied guardian id is null for student leave application : {}", leaveApplicationDTO);
                    throw new WitcurveException("Invalid Request Body");
                }
                workingDays(leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate(),leaveApplicationDTO.getSessionId(),false);
                  if(leaveApplicationRepository.findLeaveApplicationsForStudentInADateRange(leaveApplicationDTO.getSessionId() ,leaveApplicationDTO.getAppliedStudentId(),leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate()).size()!=0){
                      throw new WitcurveException("The Leave application for this student already exists in the date range !! ");
                  }
            }
            if (leaveApplicationDTO.getType().equals(LeaveApplyor.STAFF)) {
                if (leaveApplicationDTO.getAppliedStaffId() == null) {
                    log.error("Staff id is null for student leave application : {}", leaveApplicationDTO);
                    throw new WitcurveException("Invalid Request Body");
                }
                workingDays(leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate(),leaveApplicationDTO.getSessionId(),false);
                if(leaveApplicationRepository.findLeaveApplicationsForStaffInADateRange(leaveApplicationDTO.getSessionId() ,leaveApplicationDTO.getAppliedStaffId(),leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate()).size()!=0){
                    throw new WitcurveException("The Leave application for this staff already exists in the date range !! ");
                }
            }
    }
}
