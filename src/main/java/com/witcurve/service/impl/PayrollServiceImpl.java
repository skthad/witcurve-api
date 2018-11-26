package com.witcurve.service.impl;

import com.witcurve.domain.Payroll;
import com.witcurve.domain.PayrollDetails;
import com.witcurve.repository.PayrollDetailsRepository;
import com.witcurve.repository.PayrollRepository;
import com.witcurve.service.PayrollService;
import com.witcurve.service.dto.PayrollDTO;
import com.witcurve.service.dto.PayrollDetailsDTO;
import com.witcurve.service.mapper.PayrollDetailsMapper;
import com.witcurve.service.mapper.PayrollMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.List;

@Service
@Transactional
public class PayrollServiceImpl implements PayrollService {

    private final Logger log  = LoggerFactory.getLogger(PayrollServiceImpl.class);

    @Autowired
    PayrollRepository payrollRepository;

    @Autowired
    PayrollMapper payrollMapper;

    @Autowired
    PayrollDetailsRepository payrollDetailsRepository;

    @Autowired
    PayrollDetailsMapper payrollDetailsMapper;

    @Override
    public PayrollDTO saveOrUpdate(PayrollDTO payrollDTO) {
        payrollDTO.setStaffId(payrollDTO.getPayrollDetails().getStaff().getId());
        return payrollMapper.toDto(payrollRepository.save(payrollMapper.toEntity(payrollDTO)));
    }

    @Override
    public PayrollDetailsDTO saveOrUpdate(PayrollDetailsDTO payrollDetailsDTO) {
        if (payrollDetailsDTO.getId() == null) {
            payrollDetailsDTO.setDeactivationDate(null);
        }
        return payrollDetailsMapper.toDto(payrollDetailsRepository.save(payrollDetailsMapper.toEntity(payrollDetailsDTO)));
    }

    @Override
    public List<PayrollDetailsDTO> getActivePayrollDetailsForStaff(Long staffId) {
        List<PayrollDetails> result = payrollDetailsRepository.findActivePayrollDetailsForStaff(staffId);
        return payrollDetailsMapper.toDto(result);
    }

    @Override
    public List<PayrollDTO> getPayrollsForStaff(Long staffId, Long sessionId, Integer year, Month month) {
        List<Payroll> result;
        if (sessionId != null) {
            result = payrollRepository.findPayrollsForStaffInAcademicSession(staffId, sessionId);
        } else if (month == null) {
            result = payrollRepository.findPayrollsForStaffInYear(staffId, year);
        } else {
            result = payrollRepository.findActivePayrollForStaffInMonth(staffId, year, month);
        }
        return payrollMapper.toDto(result);
    }
}
