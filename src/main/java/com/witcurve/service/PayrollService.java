package com.witcurve.service;

import com.witcurve.service.dto.PayrollDTO;
import com.witcurve.service.dto.PayrollDetailsDTO;

import java.time.Month;
import java.util.List;

public interface PayrollService {

    PayrollDTO saveOrUpdate(PayrollDTO payrollDTO);

    List<PayrollDTO> getPayrollsForStaff(Long staffId, Integer year, Month month);

    List<PayrollDTO> getPayrollsForSchoolInfo(Long schoolInfoId, Long payrollCycleId);

    PayrollDetailsDTO saveOrUpdate(PayrollDetailsDTO payrollDetailsDTO);

    List<PayrollDetailsDTO> getPayrollDetailsForStaff(Long staffId, Boolean activeOnly);
}
