package com.witcurve.service;

import com.witcurve.service.dto.PayrollCycleDTO;

import java.time.Month;
import java.util.List;

public interface PayrollCycleService {

    List<PayrollCycleDTO> saveOrUpdate(List<PayrollCycleDTO> payrollCycleDTOs);

    List<PayrollCycleDTO> getPayrollCyclesForSchoolInfo(Long schoolInfoId, Integer year, Month month);

}
