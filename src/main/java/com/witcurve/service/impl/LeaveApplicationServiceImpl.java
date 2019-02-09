package com.witcurve.service.impl;

import com.witcurve.domain.AcademicSession;
import com.witcurve.domain.Event;
import com.witcurve.domain.LeaveApplication;
import com.witcurve.domain.Staff;
import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.repository.*;
import com.witcurve.service.EventService;
import com.witcurve.service.LeaveApplicationService;
import com.witcurve.service.StaffService;
import com.witcurve.service.StudentService;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.service.mapper.EventMapper;
import com.witcurve.service.mapper.LeaveApplicationMapper;
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

    public Map<Long, List<LeaveApplicationDTO>> getLeaveDetails(Long studentId,Long staffId, Long standardId, Long schoolInfoId,LocalDate fromDate, LocalDate toDate, ApprovalStatus status) throws WitcurveException {
        if (studentId == null && staffId == null && schoolInfoId == null && standardId == null) {
            throw new WitcurveException("One of studentId, standardId, staffId or schoolInfoId is required");
        }
        List<LeaveApplicationDTO> result = new ArrayList<>();
        if(status==null) {
            if (studentId != null) {
                result = leaveApplicationMapper.toDto(
                    leaveApplicationRepository.findByStudentId(studentId, fromDate, toDate));
            } else if (staffId != null) {
                result = leaveApplicationMapper.toDto(
                    leaveApplicationRepository.findByStaffId(staffId, fromDate, toDate));
            } else if (standardId != null) {
                List<Long> studentIds = studentStandardRepository.findStudentIdByStandardId(standardId);
                result = leaveApplicationMapper.toDto(
                    leaveApplicationRepository.findByStudentList(studentIds, fromDate,toDate));
            } else if (schoolInfoId != null) {
                result = leaveApplicationMapper.toDto(
                    leaveApplicationRepository.findByStaffInSchoolInfoId(schoolInfoId, fromDate,toDate));
            }
        } else if(status!= null){
            if (studentId != null) {
                result = leaveApplicationMapper.toDto(
                    leaveApplicationRepository.findByStudentAndStatus(studentId, fromDate, toDate, status));
            } else if (staffId != null) {
                result = leaveApplicationMapper.toDto(
                    leaveApplicationRepository.findByStaffIdAndStatus(staffId, fromDate, toDate, status));
            } else if (standardId != null) {
                List<Long> studentIds = studentStandardRepository.findStudentIdByStandardId(standardId);
                result = leaveApplicationMapper.toDto(
                    leaveApplicationRepository.findByStudentListAndStatus(studentIds, fromDate,toDate, status));
            } else if (schoolInfoId != null) {
                result = leaveApplicationMapper.toDto(
                    leaveApplicationRepository.findByStaffInSchoolInfoIdAndStatus(schoolInfoId, fromDate,toDate, status));
            }
        }
        if(result.size() != 0) {
            result = insertLeaveDays(result);
        }
        Map<Long, List<LeaveApplicationDTO>> resultMap = new HashMap<>();

        if (studentId != null) {
            resultMap.put(studentId, result);
        } else if (staffId != null) {
            resultMap.put(staffId, result);
        } else if (standardId != null) {
            for (LeaveApplicationDTO leaveApplicationDTO : result) {
                List<LeaveApplicationDTO> leaveApplicationDTOs = resultMap.get(leaveApplicationDTO.getAppliedStaffId());
                if (leaveApplicationDTOs == null) {
                    leaveApplicationDTOs = new ArrayList<>();
                }
                leaveApplicationDTOs.add(leaveApplicationDTO);
                resultMap.put(leaveApplicationDTO.getAppliedStudentId(), leaveApplicationDTOs);
            }
        } else if (schoolInfoId != null) {
            for (LeaveApplicationDTO leaveApplicationDTO : result) {
                List<LeaveApplicationDTO> leaveApplicationDTOs = resultMap.get(leaveApplicationDTO.getAppliedStaffId());
                if (leaveApplicationDTOs == null) {
                    leaveApplicationDTOs = new ArrayList<>();
                }
                leaveApplicationDTOs.add(leaveApplicationDTO);
                resultMap.put(leaveApplicationDTO.getAppliedStaffId(), leaveApplicationDTOs);
            }
        }
        return resultMap;
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

    private void isLeaveApplicationValid(LeaveApplicationDTO leaveApplicationDTO, Boolean update) throws WitcurveException {
        log.debug("Request to check valid leaveApplications in list : {}",leaveApplicationDTO);

        if (leaveApplicationDTO.getAppliedStaffId() != null && leaveApplicationDTO.getAppliedStudentId() != null) {
            throw new WitcurveException("Only ONE of studentId or staffId must be provided");
        } else if (leaveApplicationDTO.getAppliedStudentId() != null) {
            Long workingDays = workingDays(leaveApplicationDTO.getFromLeaveDate(),
                leaveApplicationDTO.getToLeaveDate(),leaveApplicationDTO.getSchoolInfoId(),
                false);
            if(workingDays==0){
                throw new WitcurveException("total working days is 0 so leave application cannot be created !");
            }
            List<LeaveApplication> leaveApplications = leaveApplicationRepository.
                findByStudentId(leaveApplicationDTO.getAppliedStudentId(),leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate());
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
                    throw new WitcurveException("The Leave application for this student already exists in the date range !! ");
                }
            }
        } else if (leaveApplicationDTO.getAppliedStaffId() != null) {

            Long workingDays = workingDays(leaveApplicationDTO.getFromLeaveDate(),
                leaveApplicationDTO.getToLeaveDate(),leaveApplicationDTO.getSchoolInfoId(),
                false);
            if(workingDays==0){
                throw new WitcurveException("total working days is 0 so leave application cannot be created !");
            }
            List<LeaveApplication> leaveApplications = leaveApplicationRepository.
                findByStaffId(leaveApplicationDTO.getAppliedStaffId(),leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getToLeaveDate());
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
        } else {
            throw new WitcurveException("Only ONE of studentId or staffId must be provided");
        }
    }
}
