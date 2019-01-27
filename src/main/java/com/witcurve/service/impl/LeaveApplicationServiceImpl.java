package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.AttendanceType;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.LeaveAppliedBy;
import com.witcurve.repository.*;
import com.witcurve.service.EventService;
import com.witcurve.service.LeaveApplicationService;
import com.witcurve.service.StaffService;
import com.witcurve.service.StudentService;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.service.dto.StudentDTO;
import com.witcurve.service.mapper.EventMapper;
import com.witcurve.service.mapper.LeaveApplicationMapper;
import com.witcurve.service.mapper.StaffMapper;
import com.witcurve.service.util.WeekdayUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

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

    @Autowired
    StaffService staffService;

    @Autowired
    StudentService studentService;

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
                if (isHoliday(date, leaveApplication.getSchoolInfo().getId())==false && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                        List<Event> attendance = eventRepository.findAttendanceForStudent(date, date, leaveApplicationDTO.getAppliedStudentId());
                    if(attendance.size() != 0) {
                        attendance.get(0).setName("Leave - "+leaveApplication.getReason());
                        attendance.get(0).setDescription(leaveApplication.getDescription());
                        events.add(attendance.get(0));
                    }

                }
            }
        }
            leaveApplication.setEvents(events);
            leaveApplication = leaveApplicationRepository.save(leaveApplication);
        LeaveApplicationDTO result= leaveApplicationMapper.toDto(leaveApplication);
        result.setNumLeaveDays(workingDays(leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate()
            , leaveApplicationDTO.getSchoolInfoId(),false));
        return  result;
        }

    @Override
    public LeaveApplicationDTO getLeaveApplicationById(Long leaveApplicationId) throws WitcurveException {
        log.debug("Request to get leave application with id : {}", leaveApplicationId);

        Optional<LeaveApplication> leaveApplication = leaveApplicationRepository.findById(leaveApplicationId);

        if (!leaveApplication.isPresent()) {
            throw new WitcurveException("No leave application with given id");
        }
        LeaveApplicationDTO leaveApplicationDTO= leaveApplicationMapper.toDto(leaveApplication.get());
        leaveApplicationDTO.setNumLeaveDays(workingDays(leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate(), leaveApplicationDTO.getSchoolInfoId(),false));
        return leaveApplicationDTO;
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
    public LeaveApplicationDTO changeLeaveStatus(Long leaveApplicationId,
                                                           Long staffId, ApprovalStatus status,
                                                           String note) throws WitcurveException {
        log.debug("Approval for leaveApplication with id {}, by staff id {}",leaveApplicationId, staffId);
        Optional<LeaveApplication> leaveApplication = leaveApplicationRepository.findById(leaveApplicationId);
        Optional<Staff> staff=  staffRepository.findById(staffId);
        if (!leaveApplication.isPresent()) {
            throw new WitcurveException("No leave application with given id");
        }
        if (!staff.isPresent()) {
            throw new WitcurveException("No Staff with given id");
        }
        leaveApplication.get().setStatus(status);
        leaveApplication.get().setApprovedBy(staff.get());
        leaveApplication.get().setNote(note);

        LeaveApplicationDTO leaveApplicationDTO= leaveApplicationMapper.toDto(leaveApplication.get());
        leaveApplicationDTO.setNumLeaveDays(workingDays(leaveApplicationDTO.getFromLeaveDate(),
            leaveApplicationDTO.getToLeaveDate(), leaveApplicationDTO.getSchoolInfoId(),
            false));
        return leaveApplicationDTO;
    }

    @Override
    public List<LeaveApplicationDTO> getLeaveApplicationsForStudent(Long studentId, Long sessionId, ApprovalStatus status) throws WitcurveException {
        log.debug("Get list of leaveApplication with  student id : {} and session id : {}",sessionId);
        List<LeaveApplication> leaveApplications = new ArrayList<>();
        Optional<AcademicSession> academicSession = academicSessionRepository.findById(sessionId);
        if(academicSession.isPresent()) {
            LocalDate startDate = academicSession.get().getStartDate();
            Long schoolInfoId = academicSession.get().getSchoolInfo().getId();
            if(status == null) {
                leaveApplications = leaveApplicationRepository.findBySchoolInfoIdAndAppliedStudentId(schoolInfoId,studentId, startDate);
            } else {
                leaveApplications = leaveApplicationRepository.findBySchoolInfoIdAndAppliedStudentIdWithStatus(schoolInfoId, studentId, status, startDate);
            }
            List<LeaveApplicationDTO> leaveApplicationDTOS= leaveApplicationMapper.toDto(leaveApplications);
            if(leaveApplicationDTOS.size() != 0) {
                return insertLeaveDays(leaveApplicationDTOS);
            } else {
                return leaveApplicationDTOS;
            }
        } else {
            throw new WitcurveException("Invalid session id : "+sessionId);
        }

    }

    @Override
    public List<LeaveApplicationDTO> getLeaveApplicationsForStaff(Long staffId,
                                                                  Long sessionId,
                                                                  ApprovalStatus status) throws WitcurveException {
        log.debug("Get list of leaveApplication with staff id {} and session id {}", staffId, sessionId);
        List<LeaveApplication> leaveApplications= new ArrayList<>();
        Optional<AcademicSession> academicSession = academicSessionRepository.findById(sessionId);
        if(academicSession.isPresent()) {
            LocalDate startDate = academicSession.get().getStartDate();
            Long schoolInfoId = academicSession.get().getSchoolInfo().getId();
            if(status == null) {
                leaveApplications = leaveApplicationRepository.
                    findBySchoolInfoIdAndAppliedStaffId(sessionId, staffId, startDate);
            } else {
                leaveApplications = leaveApplicationRepository.
                    findBySchoolInfoIdAndAppliedStaffIdWithStatus(sessionId, staffId, status, startDate);
            }
            List<LeaveApplicationDTO> leaveApplicationDTOS= leaveApplicationMapper.toDto(leaveApplications);
            if(leaveApplicationDTOS.size() != 0) {
                return insertLeaveDays(leaveApplicationDTOS);
            } else {
                return leaveApplicationDTOS;
            }

        } else {
            throw new WitcurveException("Invalid session id : "+sessionId);
        }
    }

    public List<LeaveApplicationDTO> getLeaveApplicationsForStandard(Long standardId, ApprovalStatus status) throws WitcurveException {
        Optional<Standard> standard = standardRepository.findById(standardId);
        if(!standard.isPresent()) {
            throw new WitcurveException("No standard exists with given id");
        }
        AcademicSession academicSession = academicSessionRepository.nearestActiveSessionToDate(standard.get().getSchoolInfo().getId(), LocalDate.now());
        if(academicSession == null) {
            throw new WitcurveException("No active session  for standard: "+standardId);
        }
        Long schoolInfoId = academicSession.getSchoolInfo().getId();
        LocalDate startDate = academicSession.getStartDate();

        List<LeaveApplication> leaveApplications = new ArrayList<>();
        List<Long> studentIds = studentStandardRepository.findStudentIdByStandardId(standardId);
        if(studentIds.size() !=0) {
            if(status == null) {
                leaveApplications = leaveApplicationRepository.findLeaveAppicationsForStudentsForSchoolInfoId(schoolInfoId, studentIds, startDate);
            } else {
                leaveApplications = leaveApplicationRepository.findLeaveAppicationsForStudentsForSchoolInfoIdWithStatus(schoolInfoId, studentIds, status, startDate);
            }
        }
        List<LeaveApplicationDTO> leaveApplicationDTOS= leaveApplicationMapper.toDto(leaveApplications);
        if(leaveApplicationDTOS.size() != 0) {
            return insertLeaveDays(leaveApplicationDTOS);
        } else {
            return leaveApplicationDTOS;
        }
    }

    public List<LeaveApplicationDTO> getAppliedLeaveDetails(Long studentId,Long staffId,Long schoolInfoId,LocalDate fromDate, LocalDate toDate) throws WitcurveException {
        if (studentId == null && staffId == null && schoolInfoId == null) {
            throw new WitcurveException("Student ID and staff ID and schoolInfoId all cannot be null");
        }
        List<LeaveApplication> result = new ArrayList<>();
        if(studentId !=null) {
            result = leaveApplicationRepository.findLeaveApplicationsForStudentInADateRange(schoolInfoId, studentId, fromDate, toDate);
        } else if(staffId != null){
            result = leaveApplicationRepository.findLeaveApplicationsForStaffInADateRange(schoolInfoId, staffId, fromDate, toDate);
        } else if(schoolInfoId != null){
            AcademicSession academicSession= academicSessionRepository.nearestActiveSessionToDate(schoolInfoId,LocalDate.now());
            LocalDate startDate= academicSession.getStartDate();
            result=leaveApplicationRepository.findLeaveApplicationsForAllStaffsInSchool(schoolInfoId,startDate,ApprovalStatus.PENDING);
        }

        return leaveApplicationMapper.toDto(result);
    }

    private List<LeaveApplicationDTO> insertLeaveDays(List<LeaveApplicationDTO> leaveApplicationDTOS) throws WitcurveException {
        if(leaveApplicationDTOS.size()!= 0) {
            if (leaveApplicationDTOS == null) {
                throw new WitcurveException("DTO is null");
            }
            for (int i = 0; i < leaveApplicationDTOS.size(); i++) {
            leaveApplicationDTOS.get(i).setNumLeaveDays(workingDays(leaveApplicationDTOS.get(i).getFromLeaveDate(), leaveApplicationDTOS.get(i).getToLeaveDate(),
                leaveApplicationDTOS.get(i).getSchoolInfoId(), false));
            }
            return leaveApplicationDTOS;
        }
        else {
            throw new WitcurveException("Size of leave application list is 0 !!");
        }
    }
    private Boolean isHoliday(LocalDate date, Long schoolInfoId) throws WitcurveException {

        if(eventRepository.findHolidaysBetweenFromDateAndToDate(date,date, schoolInfoId)==1)
            return true;
        else
            return false;
    }

    public Long workingDays(LocalDate fromDate, LocalDate toDate, Long schoolInfoId, Boolean isSaturdayWorking)
        throws WitcurveException {
        AcademicSession academicSession = academicSessionRepository.nearestActiveSessionToDate(schoolInfoId, fromDate);
        if(academicSession == null) {
            throw new WitcurveException("session id not present !");
        }
        LocalDate startDate= academicSession.getStartDate();
        LocalDate endDate = startDate.plusYears(1);
        Long workingDays = DAYS.between(fromDate, toDate) + 1;
        Long noOfSundays = 0L;
        Long noOfSaturdays = 0L;
        if (fromDate.isAfter(toDate)) {
            throw new WitcurveException("from date cannot be after to date.");
        }
        if (fromDate.isAfter(startDate.minusDays(1)) && toDate.isBefore(endDate.plusDays(1))) {
            noOfSundays = WeekdayUtil.getNoOfWeekDayBetweenDates(fromDate, toDate, DayOfWeek.SUNDAY);
            if(!isSaturdayWorking) {
                noOfSaturdays = WeekdayUtil.getNoOfWeekDayBetweenDates(fromDate, toDate, DayOfWeek.SATURDAY);
            }
            // to remove the holidays
            Long holidays = eventRepository.findHolidaysBetweenFromDateAndToDate(fromDate,toDate, schoolInfoId);
            workingDays = workingDays - noOfSundays - noOfSaturdays - holidays;
        }
        else
        {
            throw new WitcurveException("start date or end date are out of academic session");
        }
        return workingDays;
    }

    private Event createEvent(LeaveApplicationDTO leaveApplicationDTO,LocalDate date){
        LeaveApplication leaveApplications = leaveApplicationMapper.toEntity(leaveApplicationDTO);
        Event event = new Event();
        event.setName("Leave - "+leaveApplications.getReason());
        event.setType(EventType.ATTENDANCE);
        event.setAttendanceType(AttendanceType.ABSENT);
        if(leaveApplicationDTO.getType().equals(LeaveAppliedBy.STUDENT)) {
        event.setStudent(leaveApplications.getAppliedStudent());}
        else if(leaveApplicationDTO.getType().equals(LeaveAppliedBy.STAFF)){
        event.setStaff(leaveApplications.getAppliedStaff()); }
        //event.setAcademicSession(leaveApplications.getSession());
        event.setDate(date);
        //eventDTO.add(eventMapper.toDto(event));
        //eventService.saveOrUpdate(eventDTO);
        eventRepository.save(event);
        return event;
    }

    private void isLeaveApplicationValid(LeaveApplicationDTO leaveApplicationDTO, Boolean update) throws WitcurveException {
        log.debug("Request to check valid leaveApplications in list : {}",leaveApplicationDTO);
            if (leaveApplicationDTO.getType().equals(LeaveAppliedBy.STUDENT)) {
                // add the condition for guardian id later when guardian id is made required
//                if (leaveApplicationDTO.getAppliedStudentId() == null || leaveApplicationDTO.getAppliedGuardianId() == null) {
//                    log.error("There either applied student id or applied guardian id is null for student leave application : {}", leaveApplicationDTO);
//                    throw new WitcurveException("Invalid Request Body");
//                }
                if (leaveApplicationDTO.getAppliedStudentId() == null) {
                    log.error("There either applied student id  for student leave application : {}", leaveApplicationDTO);
                    throw new WitcurveException("Invalid Request Body");
                }
                Long workingDays = workingDays(leaveApplicationDTO.getFromLeaveDate(),
                    leaveApplicationDTO.getToLeaveDate(),leaveApplicationDTO.getSchoolInfoId(),
                    false);
                if(workingDays==0){
                    throw new WitcurveException("total working days is 0 so leave application cannot be created !");
                }
                List<LeaveApplication> leaveApplications = leaveApplicationRepository.
                    findLeaveApplicationsForStudentInADateRange(leaveApplicationDTO.getSchoolInfoId(),
                        leaveApplicationDTO.getAppliedStudentId(),leaveApplicationDTO.getFromLeaveDate(),
                        leaveApplicationDTO.getToLeaveDate());
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
            if (leaveApplicationDTO.getType().equals(LeaveAppliedBy.STAFF)) {
                if (leaveApplicationDTO.getAppliedStaffId() == null) {
                    log.error("Staff id is null for student leave application : {}", leaveApplicationDTO);
                    throw new WitcurveException("Invalid Request Body");
                }
                Long workingDays = workingDays(leaveApplicationDTO.getFromLeaveDate(),
                    leaveApplicationDTO.getToLeaveDate(),leaveApplicationDTO.getSchoolInfoId(),
                    false);
                if(workingDays==0){
                    throw new WitcurveException("total working days is 0 so leave application cannot be created !");
                }
                List<LeaveApplication> leaveApplications = leaveApplicationRepository.
                    findLeaveApplicationsForStaffInADateRange(leaveApplicationDTO.getSchoolInfoId(),
                        leaveApplicationDTO.getAppliedStaffId(),leaveApplicationDTO.getFromLeaveDate(),
                        leaveApplicationDTO.getToLeaveDate());
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
