package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.domain.Staff;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.StaffRepository;
import com.witcurve.repository.UserRepository;
import com.witcurve.service.StaffService;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.service.mapper.StaffMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public StaffDTO create(StaffDTO staffDTO) {
        log.debug("Request to create staff : {}", staffDTO);

        User user = new User();
        user.setLogin(staffDTO.getSchoolInfo().getId() + "-" + staffDTO.getStaffId());
        user.setFirstName(staffDTO.getFirstName());
        user.setLastName(staffDTO.getLastName());
        user.setType(UserType.STAFF);
        user.setActivated(false);
        user = userRepository.save(user);
        Staff staff = staffMapper.toEntity(staffDTO);
        staff.setUser(user);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    @Override
    public StaffDTO update(StaffDTO staffDTO) {
        log.debug("Request to update staff : {}", staffDTO);
        Staff staff = staffMapper.toEntity(staffDTO);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    @Override
    public StaffDTO getStaffById(Long staffId) throws WitcurveException {
        log.debug("Request to get staff with id : {}", staffId);
        Staff staff = staffRepository.findById(staffId).get();
        if (staff == null) {
            throw new WitcurveException("No staff exists with given id");
        }
        StaffDTO staffDTO = staffMapper.toDto(staff);
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(staff.getUser().getId());
//        staffDTO.setUser(userDTO);
        return staffDTO;
    }

    @Override
    public StaffDTO getStaffByUserId(Long userId) throws WitcurveException {
        log.debug("Request to get staff with user id : {}", userId);
        Staff staff = staffRepository.getStaffByUserId(userId);
        if (staff == null) {
            throw new WitcurveException("No staff exists with given id");
        }
        return staffMapper.toDto(staff);
    }

    @Override
    public void deleteStaffById(Long staffId) throws WitcurveException {
        log.debug("Request to delete staff with id : {}", staffId);
        Staff staff = staffRepository.findById(staffId).get();
        if (staff == null) {
            throw new WitcurveException("No staff exists with given id");
        }
        staffRepository.delete(staff);
    }

    @Override
    public StaffDTO getStaffBySchoolIdAndStaffId(Long schoolId, String staffId) throws WitcurveException {
        log.debug("Request to get staff with school id : {} and staff id : {}", schoolId, staffId);
        Staff staff = staffRepository.findBySchoolIdAndStaffId(schoolId, staffId.toLowerCase());
        if (staff == null){
            throw  new WitcurveException("No staff with given staff id in the give school info id");
        }
        StaffDTO result = staffMapper.toDto(staff);
        if (Strings.isNullOrEmpty(staff.getUser().getPassword())) {
            result.setHasPassword(Boolean.FALSE);
        } else {
            result.setHasPassword(Boolean.TRUE);
        }
        return result;
    }

    @Override
    public List<StaffDTO> getStaffBySchoolId(Long schoolId) {
        log.debug("Request to get staff with school id : {} ", schoolId);
        List<Staff> staffList = staffRepository.findBySchoolId(schoolId);
        List<StaffDTO> result = staffMapper.toDto(staffList);
        return result;
    }
}
