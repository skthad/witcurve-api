package com.witcurve.service.impl;

import com.witcurve.domain.Leave;
import com.witcurve.repository.LeaveRepository;
import com.witcurve.service.LeaveService;
import com.witcurve.service.dto.LeaveDTO;
import com.witcurve.service.mapper.LeaveMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final Logger log  = LoggerFactory.getLogger(LeaveServiceImpl.class);


    @Autowired
    LeaveRepository leaveRepository;

    @Autowired
    LeaveMapper leaveMapper;


    @Override
    public LeaveDTO saveOrUpdate(LeaveDTO leaveDTO) {
        log.debug("Request to save or update leave : {}", leaveDTO);
        Leave leave = leaveMapper.leaveDTOToLeave(leaveDTO);
        leave = leaveRepository.save(leave);
        return leaveMapper.leaveToLeaveDTO(leave);
    }

    @Override
    public LeaveDTO getLeaveById(Long leaveId) throws WitcurveException {
        log.debug("Request to get holiday with id : {}", leaveId);
        Leave leave = leaveRepository.findById(leaveId).get();

        if (leave ==  null) {
            throw new WitcurveException("No Leave with given Id");
        }
        return leaveMapper.leaveToLeaveDTO(leave);
    }

    @Override
    public void deleteLeave(Long leaveId) throws WitcurveException {
        log.debug("Request to delete holiday with id : {}", leaveId);
        Leave leave = leaveRepository.findById(leaveId).get();

        if (leave == null){
            throw new WitcurveException("No leave with given Id");
        }

        leaveRepository.delete(leave);
    }
}
