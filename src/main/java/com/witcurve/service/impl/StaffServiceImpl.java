package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.Staff;
import com.witcurve.domain.StaffEligibility;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.StaffType;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.*;
import com.witcurve.service.StaffEligibilityService;
import com.witcurve.service.StaffService;
import com.witcurve.service.UserService;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.service.dto.UserDTO;
import com.witcurve.service.mapper.StaffMapper;
import com.witcurve.service.mapper.UserMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final Logger log = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StaffMapper staffMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    StaffEligibilityRepository staffEligibilityRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    StaffEligibilityService staffEligibilityService;

    @Override
    public StaffDTO create(StaffDTO staffDTO) {
        log.debug("Request to create staff : {}", staffDTO);
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(staffDTO.getSchoolInfo().getId() + "-" + staffDTO.getEmployeeId());
        userDTO.setFirstName(staffDTO.getFirstName());
        userDTO.setLastName(staffDTO.getLastName());
        userDTO.setEmail(staffDTO.getEmail());
        if (StaffType.TEACHING.equals(staffDTO.getType())) {
            userDTO.setType(UserType.TEACHING_STAFF);
        } else {
            userDTO.setType(UserType.NON_TEACHING_STAFF);
        }
        User user = userService.createUser(userDTO);
        Staff staff = staffMapper.toEntity(staffDTO);
        staff.setUser(user);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    @Override
    public StaffDTO update(StaffDTO staffDTO) throws WitcurveException {
        log.debug("Request to update staff : {}", staffDTO);
        Optional<User> user = userRepository.findById(staffDTO.getUserId());
        if(!user.isPresent()) {
            throw new WitcurveException("There is no user with given id : "+staffDTO.getUserId());
        }
        UserDTO userDTO = userMapper.userToUserDTO(user.get());
        userDTO.setLogin(staffDTO.getSchoolInfo().getId() + "-" + staffDTO.getEmployeeId());
        userDTO.setFirstName(staffDTO.getFirstName());
        userDTO.setLastName(staffDTO.getLastName());
        userDTO.setEmail(staffDTO.getEmail());
        if (StaffType.TEACHING.equals(staffDTO.getType())) {
            userDTO.setType(UserType.TEACHING_STAFF);
        } else {
            userDTO.setType(UserType.NON_TEACHING_STAFF);
        }
        userService.updateUser(userDTO);
        Staff staff = staffMapper.toEntity(staffDTO);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    @Override
    public StaffDTO getStaffById(Long staffId) throws WitcurveException {
        log.debug("Request to get staff with id : {}", staffId);
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (!staff.isPresent()) {
            throw new WitcurveException("No staff found");
        }
        StaffDTO staffDTO = staffMapper.toDto(staff.get());
        return staffDTO;
    }

    @Override
    public StaffDTO getStaffByUserId(Long userId) throws WitcurveException {
        log.debug("Request to get staff with user id : {}", userId);
        Staff staff = staffRepository.getStaffByUserId(userId);
        if (staff == null) {
            throw new WitcurveException("No staff exists with given user id");
        }
        return staffMapper.toDto(staff);
    }

    @Override
    public StaffDTO getStaffByUsername(String username) throws WitcurveException {
        log.info("Request to get staff with username : {}", username);
        int index = username.indexOf("-");
        if(index >  0 ) {
            Long schoolInfoId;
            try {
                schoolInfoId = Long.parseLong(username.substring(0, index));
            } catch (NumberFormatException e) {
                log.error("Entered school info id in user name is wrong : {}", username);
                throw new WitcurveException("Invalid username, please enter the correct username");
            }
            String staffId = username.substring(index + 1);
            Staff staff = staffRepository.findBySchoolInfoIdAndStaffId(schoolInfoId, staffId.toLowerCase());
            if (staff == null){
                log.error("No staff with given staff id : {} in the give school info id : {}", staffId, schoolInfoId);
                throw  new WitcurveException("No staff exists with given username");
            }
            StaffDTO result = staffMapper.toDto(staff);
            if (Strings.isNullOrEmpty(staff.getUser().getPassword())) {
                result.setHasPassword(Boolean.FALSE);
            } else {
                result.setHasPassword(Boolean.TRUE);
            }
            return result;
        } else {
            log.error("Entered user name is not in format of schoolInfoId-StaffId for username : {}", username);
            throw new WitcurveException("Invalid username, please enter the correct username");
        }

    }

    @Override
    public List<StaffDTO> getStaffBySchoolId(Long schoolId) {
        log.debug("Request to get staff with school id : {} ", schoolId);
        List<Staff> staffList = staffRepository.findBySchoolId(schoolId);
        List<StaffDTO> result = staffMapper.toDto(staffList);
        return result;
    }

    @Override
    public List<StaffDTO> getStaffBySchoolInfoId(Long schoolInfoId, Boolean areClassTeacher, Boolean activated, StaffType type) {
        log.debug("Request to get staff with schoolInfo id : {} ", schoolInfoId, areClassTeacher);
        List<Staff> staffList = null;
        Optional<SchoolInfo> schoolInfo = schoolInfoRepository.findById(schoolInfoId);
        if (!schoolInfo.isPresent()) {
            throw new WitcurveException("No SchoolInfo with given id " + schoolInfoId);
        }
        if(areClassTeacher) {
            staffList = staffRepository.findClassTeachersBySchoolInfoId(schoolInfoId);
        } else if(!activated) {
            staffList = staffRepository.findDeletedTeachersBySchoolInfoId(schoolInfoId);
        } else if(type != null) {
            staffList = staffRepository.findBySchoolInfoIdAndType(schoolInfoId, type);
        } else {
            staffList = staffRepository.findBySchoolInfoId(schoolInfoId);
        }
        List<StaffEligibility> staffEligibility = staffEligibilityRepository.findBySchoolInfo(schoolInfoId);
        Map<Long, Set<String>> subjectMap = new HashMap<>();
        for (StaffEligibility se : staffEligibility) {
            Long staffId = se.getStaff().getId();
            if (subjectMap.get(staffId) == null) {
                subjectMap.put(staffId, new HashSet<>());
            }
            subjectMap.get(staffId).add(se.getMasterSubject().getName());
        }
        List<StaffDTO> result = staffMapper.toDto(staffList);
        for (StaffDTO staff : result) {
            staff.setSubjects(subjectMap.get(staff.getId()));
        }
        return result;
    }

    @Override
    public void deactivate(Long staffId) throws WitcurveException {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (staff.isPresent()) {
            if(StaffType.TEACHING.equals(staff.get().getType())) {
                int activeCourseTeacherCount = courseTeacherRepository.findByTeacherId(staffId).size();
                if (activeCourseTeacherCount > 0) {
                    throw new WitcurveException("This staff is already linked to active courses. Please deactive before proceeding.");
                }
                staffEligibilityRepository.deleteByStaffId(staffId);
            }
            staff.get().getUser().setActivated(false);
        } else {
            throw new WitcurveException("No staff found");
        }
    }

    @Override
    public void activate(Long staffId) throws WitcurveException {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (staff.isPresent()) {
            staff.get().getUser().setActivated(true);
        } else {
            throw new WitcurveException("No staff found");
        }
    }


}
