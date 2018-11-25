package com.witcurve.service;

import com.witcurve.service.dto.PayrollDTO;
import com.witcurve.service.dto.PayrollDetailsDTO;

import java.time.Month;
import java.util.List;

public interface PayrollService {

    PayrollDTO saveOrUpdate(PayrollDTO payrollDTO);

    List<PayrollDTO> getPayrollsForStaff(Long staffId, Long sessionId, Integer year, Month month);

    PayrollDetailsDTO saveOrUpdate(PayrollDetailsDTO payrollDetailsDTO);

    List<PayrollDetailsDTO> getActivePayrollDetailsForStaff(Long staffId);
}
