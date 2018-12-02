package com.witcurve.service.impl;

import com.witcurve.domain.AcademicSession;
import com.witcurve.domain.LeaveApplication;
import com.witcurve.domain.Staff;
import com.witcurve.domain.Standard;
import com.witcurve.domain.enumeration.LeaveApplyor;
import com.witcurve.repository.*;
import com.witcurve.service.LeaveApplicationService;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.service.mapper.LeaveApplicationMapper;
import com.witcurve.service.util.WorkingDaysUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public LeaveApplicationDTO saveOrUpdate(LeaveApplicationDTO leaveApplicationDTOs) throws WitcurveException {
        log.debug("Request to save or update leave applications : {}", leaveApplicationDTOs.toString());

        isLeaveApplicationValid(leaveApplicationDTOs);
        LeaveApplication leaveApplications = leaveApplicationMapper.toEntity(leaveApplicationDTOs);
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
            leaveApplications = leaveApplicationRepository.findBySessionIdAndAppliedStudentIdOrderByFromLeaveDate(sessionId,studentId);
        } else {
            if(approved) {
                leaveApplications = leaveApplicationRepository.findBySessionIdAndAppliedStudentIdAndApprovedTrueOrderByFromLeaveDate(sessionId, studentId);
            } else {
                leaveApplications = leaveApplicationRepository.findBySessionIdAndAppliedStudentIdAndApprovedFalseOrderByFromLeaveDate(sessionId, studentId);
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


    public Long getLeaveCount(Long leaveApplicationId,Long sessionId, Boolean isSaturdayWorking) throws WitcurveException {
        log.debug("Request to count number of working days for leave Application with id {}", leaveApplicationId);
        Optional<LeaveApplication> leaveApplication = leaveApplicationRepository.findById(leaveApplicationId);
        Long workingDays= (Long) workingDays(leaveApplication.get().getFromLeaveDate(), leaveApplication.get().getToLeaveDate(),sessionId,isSaturdayWorking);
        return workingDays;
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


    private void isLeaveApplicationValid(LeaveApplicationDTO leaveApplicationDTO) throws WitcurveException {
        log.debug("Request to check valid leaveApplications in list : {}",leaveApplicationDTO);
        LeaveApplication leaveApplications = leaveApplicationMapper.toEntity(leaveApplicationDTO);
            if (leaveApplicationDTO.getType().equals(LeaveApplyor.STUDENT)) {
                if (leaveApplicationDTO.getAppliedStudentId() == null || leaveApplicationDTO.getAppliedGuardianId() == null) {
                    log.error("There either applied student id or applied guardian id is null for student leave application : {}", leaveApplicationDTO);
                    throw new WitcurveException("Invalid Request Body");
                }
                workingDays(leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getSessionId(),false);
                  List<LeaveApplication> existingLeaveApplicationsForOneStudent = leaveApplicationRepository.findBySessionIdAndAppliedStudentIdAndApprovedTrueOrderByFromLeaveDate(leaveApplicationDTO.getSessionId() ,leaveApplicationDTO.getAppliedStudentId());
                  for(LeaveApplication leaveApplication:existingLeaveApplicationsForOneStudent){
                                    if(leaveApplications.getFromLeaveDate().isAfter(leaveApplication.getFromLeaveDate())
                                    && leaveApplications.getFromLeaveDate().isBefore(leaveApplication.getToLeaveDate())) {
                                        throw new WitcurveException("Leave Application already exists with id "+ leaveApplication.getId()
                                        +"from date :"+ leaveApplication.getFromLeaveDate()+"end date"+ leaveApplication.getToLeaveDate());
                                    }
                                    if(leaveApplications.getToLeaveDate().isAfter(leaveApplication.getFromLeaveDate())
                                        && leaveApplications.getToLeaveDate().isBefore(leaveApplication.getToLeaveDate())){
                                        throw new WitcurveException("Leave Application already exists for student id :"+leaveApplicationDTO.getAppliedStudentId()+ "with id "+ leaveApplication.getId()
                                            +"from date :"+ leaveApplication.getFromLeaveDate()+"end date"+ leaveApplication.getToLeaveDate());
                                    }
                                    if(leaveApplications.getFromLeaveDate().isEqual(leaveApplication.getFromLeaveDate()) ||
                                        leaveApplications.getFromLeaveDate().isEqual(leaveApplication.getToLeaveDate())){
                                        throw new WitcurveException("Leave Application already exists for student id :"+leaveApplicationDTO.getAppliedStudentId()+ "with id "+ leaveApplication.getId()
                                            +"from date :"+ leaveApplication.getFromLeaveDate()+"end date"+ leaveApplication.getToLeaveDate());
                                    }
                                    if(leaveApplications.getToLeaveDate().isEqual(leaveApplication.getFromLeaveDate()) ||
                                         leaveApplications.getToLeaveDate().isEqual(leaveApplication.getToLeaveDate())){
                                         throw new WitcurveException("Leave Application already exists for student id :"+leaveApplicationDTO.getAppliedStudentId()+ " with id "+ leaveApplication.getId()
                                            +"from date :"+ leaveApplication.getFromLeaveDate()+"end date"+ leaveApplication.getToLeaveDate());
                      }

                  }
            }
            if (leaveApplicationDTO.getType().equals(LeaveApplyor.STAFF)) {
                if (leaveApplicationDTO.getAppliedStaffId() == null) {
                    log.error("Staff id is null for student leave application : {}", leaveApplicationDTO);
                    throw new WitcurveException("Invalid Request Body");
                }
                workingDays(leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getFromLeaveDate(),leaveApplicationDTO.getSessionId(),false);
                List<LeaveApplication> existingLeaveApplicationsForOneStaff = leaveApplicationRepository.findBySessionIdAndAppliedStaffIdAndApprovedTrueOrderByFromLeaveDate(leaveApplicationDTO.getSessionId() ,leaveApplicationDTO.getAppliedStudentId());
                for(LeaveApplication leaveApplication:existingLeaveApplicationsForOneStaff) {
                    if (leaveApplications.getFromLeaveDate().isAfter(leaveApplication.getFromLeaveDate())
                        && leaveApplications.getFromLeaveDate().isBefore(leaveApplication.getToLeaveDate())) {
                        throw new WitcurveException("Leave Application already exists for staff id :" + leaveApplicationDTO.getAppliedStaffId() + "with id " + leaveApplication.getId()
                            + "from date :" + leaveApplication.getFromLeaveDate() + "end date" + leaveApplication.getToLeaveDate());
                    }
                    if (leaveApplications.getToLeaveDate().isAfter(leaveApplication.getFromLeaveDate())
                        && leaveApplications.getToLeaveDate().isBefore(leaveApplication.getToLeaveDate())) {
                        throw new WitcurveException("Leave Application already exists for staff id :" + leaveApplicationDTO.getAppliedStaffId() + "with id " + leaveApplication.getId()
                            + "from date :" + leaveApplication.getFromLeaveDate() + "end date" + leaveApplication.getToLeaveDate());
                    }
                    if (leaveApplications.getFromLeaveDate().isEqual(leaveApplication.getFromLeaveDate()) ||
                        leaveApplications.getFromLeaveDate().isEqual(leaveApplication.getToLeaveDate())) {
                        throw new WitcurveException("Leave Application already exists for staff id :" + leaveApplicationDTO.getAppliedStaffId() + "with id " + leaveApplication.getId()
                            + "from date :" + leaveApplication.getFromLeaveDate() + "end date" + leaveApplication.getToLeaveDate());
                    }
                    if (leaveApplications.getToLeaveDate().isEqual(leaveApplication.getFromLeaveDate()) ||
                        leaveApplications.getToLeaveDate().isEqual(leaveApplication.getToLeaveDate())) {
                        throw new WitcurveException("Leave Application already exists for staff id :" + leaveApplicationDTO.getAppliedStaffId() + " with application id " + leaveApplication.getId()
                            + "from date :" + leaveApplication.getFromLeaveDate() + "end date" + leaveApplication.getToLeaveDate());
                    }
                }
            }
    }
}
