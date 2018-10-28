package com.witcurve.service.impl;
import com.witcurve.domain.AcademicSession;
import com.witcurve.domain.Staff;
import com.witcurve.domain.Student;
import com.witcurve.repository.AcademicSessionRepository;
import com.witcurve.repository.StaffRepository;
import com.witcurve.repository.StudentRepository;
import com.witcurve.service.LeaveApplicationService;
import com.witcurve.service.mapper.StaffMapper;
import com.witcurve.service.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import com.witcurve.domain.LeaveApplication;
import com.witcurve.repository.LeaveApplicationRepository;
import com.witcurve.service.dto.LeaveApplicationDTO;
import com.witcurve.service.mapper.LeaveApplicationMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    private final Logger log  = LoggerFactory.getLogger(CourseServiceImpl.class);

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

    @Override
    public List<LeaveApplicationDTO> saveOrUpdate(List<LeaveApplicationDTO> leaveApplicationDTOs) throws WitcurveException {
        log.debug("Request to save or update Course: {}", leaveApplicationDTOs);
        isLeaveApplicationValid(leaveApplicationDTOs);

        if (leaveApplicationDTOs.size() > 1) {
            String bindingId = UUID.randomUUID().toString();
            for(LeaveApplicationDTO leaveApplicationDTO :leaveApplicationDTOs)
            {
                leaveApplicationDTO.setBindingId(bindingId);
            }
        }
            List<LeaveApplication> leaveApplication = leaveApplicationMapper.toEntity(leaveApplicationDTOs);
            leaveApplication = leaveApplicationRepository.saveAll(leaveApplication);
            return  leaveApplicationMapper.toDto(leaveApplication);
        }

    @Override
    public LeaveApplicationDTO getLeaveApplicationById(Long leaveApplicationId) throws WitcurveException {
        log.debug("Request to get leave application with id : {}", leaveApplicationId);

        LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveApplicationId).get();

        if (leaveApplication ==  null) {
            throw new WitcurveException("No leave application with given id");
        }
        return leaveApplicationMapper.toDto(leaveApplication);
    }

    @Override
    public void deleteLeaveApplication(Long leaveApplicationId) throws WitcurveException {
        log.debug("Request to delete leave Application with id {}", leaveApplicationId);
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveApplicationId).get();
        if (leaveApplication == null){
            throw new WitcurveException("No leave application with given Id");
        }
        leaveApplicationRepository.delete(leaveApplication);

    }

    @Override
    public LeaveApplicationDTO getLeaveApplicationApprover(Long leaveApplicationId,Long staffId) throws WitcurveException {
        log.debug("Approval for leaveApplication with id {}", leaveApplicationId, "by staff id {}",staffId);
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveApplicationId).get();
        Staff staff=  staffRepository.findById(staffId).get();
        if (leaveApplication == null) {
            throw new WitcurveException("No leave application with given Id");
        }
        if (staff == null) {
            throw new WitcurveException("No Staff with given Id");
        }
        leaveApplication.setApproved(true);
        leaveApplication.setApprovedBy(staff);
      return leaveApplicationMapper.toDto(leaveApplication);
    }
    @Override
    public List<LeaveApplicationDTO> getLeaveApplicationsForStudent(Long studentId,Long sessionId) throws WitcurveException {
        log.debug("Get list of leaveApplication with id {}", studentId, "with sessionId {}",sessionId);
        List<LeaveApplication> leaveApplication= leaveApplicationRepository.findBySessionIdAndAppliedStudentId(sessionId,studentId);
        return leaveApplicationMapper.toDto(leaveApplication);
    }

    @Override
    public List<LeaveApplicationDTO> getLeaveApplicationsForStaff(Long staffId,Long sessionId) throws WitcurveException {
        log.debug("Get list of leaveApplication with id {}", staffId, "with sessionId {}",sessionId);
        List<LeaveApplication> leaveApplication= leaveApplicationRepository.findBySessionIdAndAppliedStaffId(sessionId,staffId);
        return leaveApplicationMapper.toDto(leaveApplication);
    }

    private void isLeaveApplicationValid(List<LeaveApplicationDTO> leaveApplicationDTOs) throws WitcurveException {
        log.debug("Request to check staffId, guardianId,studentId: {}", leaveApplicationDTOs);
        for (LeaveApplicationDTO leaveApplicationDTO : leaveApplicationDTOs) {
            if (leaveApplicationDTO.getAppliedStudentId() != null) {
                if(!leaveApplicationRepository.findByAppliedStudentIdAndLeaveDate(leaveApplicationDTO.getAppliedStudentId(), leaveApplicationDTO.getLeaveDate()).isEmpty())
                {
                throw new WitcurveException("Already exists for the student id on todays date ");
            }
        }
            if (leaveApplicationDTO.getAppliedStaffId() != null) {
                if (!leaveApplicationRepository.findByAppliedStaffIdAndLeaveDate(leaveApplicationDTO.getAppliedStaffId(), leaveApplicationDTO.getLeaveDate()).isEmpty()) {
                    throw new WitcurveException("Already exists for the staff id on todays date ");
                }
            }
            if (leaveApplicationDTO.getType().equalsIgnoreCase("staff") && leaveApplicationDTO.getAppliedStaffId() == null) {
                log.error("No such staff exists with id :" + leaveApplicationDTO.getBindingId());
                throw new WitcurveException("Invalid id ");
            }
                if (leaveApplicationDTO.getType().equalsIgnoreCase("student") && leaveApplicationDTO.getAppliedStudentId() == null) {
                    log.error("No such student exists with id :" + leaveApplicationDTO.getBindingId());
                    throw new WitcurveException("Invalid id ");
                }
                if (leaveApplicationDTO.getType().equalsIgnoreCase("student") && leaveApplicationDTO.getAppliedGuardianId() == null) {
                    log.error("No such guardian exists with id :" + leaveApplicationDTO.getBindingId());
                    throw new WitcurveException("Invalid id");
                }
        }
    }
}
