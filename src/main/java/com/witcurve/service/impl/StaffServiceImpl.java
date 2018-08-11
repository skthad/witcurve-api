package com.witcurve.service.impl;

import com.witcurve.domain.Staff;
import com.witcurve.repository.StaffRepository;
import com.witcurve.service.StaffService;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.service.mapper.StaffMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements StaffService {

    private final Logger log  = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StaffMapper staffMapper;


    @Override
    public StaffDTO saveOrUpdate(StaffDTO staffDTO) {
        log.debug("Request to save or update staff : {}", staffDTO);
        Staff staff = staffMapper.staffDTOToStaff(staffDTO);
       staff = staffRepository.save(staff);
       return staffMapper.staffToStaffDTO(staff);
    }

    @Override
    public StaffDTO getStaffById(Long staffId) throws WitcurveException {
        log.debug("Request to get staff with id : {}", staffId);
        Staff staff = staffRepository.findById(staffId).get();
        if (staff == null) {
            throw  new WitcurveException("No staff exists with given id");
        }
        return staffMapper.staffToStaffDTO(staff);
    }

    @Override
    public void deleteStaffById(Long staffId) throws WitcurveException {
        log.debug("Request to delete staff with id : {}", staffId);
        Staff staff = staffRepository.findById(staffId).get();
        if (staff == null) {
            throw  new WitcurveException("No staff exists with given id");
        }
        staffRepository.delete(staff);
    }
}
