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
import com.witcurve.service.util.PayrollComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.Collections;
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
    public List<PayrollDTO> getPayrollsForStaff(Long staffId, Integer year, Month month) {
        List<Payroll> payrolls;
        if (month != null) {
            payrolls = payrollRepository.findPayrollForStaffInMonth(staffId, year, month);
        } else if (year != null) {
            payrolls = payrollRepository.findPayrollsForStaffInYear(staffId, year);
        } else {
            payrolls = payrollRepository.findAllPayrollsForStaff(staffId);
        }
        List<PayrollDTO> result = payrollMapper.toDto(payrolls);
        Collections.sort(result, new PayrollComparator());
        return result;
    }

    @Override
    public List<PayrollDTO> getPayrollsForSchoolInfo(Long schoolInfoId, Integer year, Month month) {
        List<Payroll> payrolls;
        if (month != null) {
            payrolls = payrollRepository.findPayrollForScoolInfoInMonth(schoolInfoId, year, month);
        } else if (year != null) {
            payrolls = payrollRepository.findPayrollsForScoolInfoInYear(schoolInfoId, year);
        } else {
            payrolls = payrollRepository.findAllPayrollsForScoolInfo(schoolInfoId);
        }
        List<PayrollDTO> result = payrollMapper.toDto(payrolls);
        Collections.sort(result, new PayrollComparator());
        return result;
    }
}
