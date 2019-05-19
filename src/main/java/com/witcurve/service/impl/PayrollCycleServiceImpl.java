package com.witcurve.service.impl;

import com.witcurve.domain.PayrollCycle;
import com.witcurve.repository.PayrollCycleRepository;
import com.witcurve.service.PayrollCycleService;
import com.witcurve.service.dto.PayrollCycleDTO;
import com.witcurve.service.mapper.PayrollCycleMapper;
import com.witcurve.service.util.PayrollCycleComparator;
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
public class PayrollCycleServiceImpl implements PayrollCycleService {

    private final Logger log  = LoggerFactory.getLogger(PayrollCycleServiceImpl.class);

    @Autowired
    PayrollCycleRepository payrollCycleRepository;

    @Autowired
    PayrollCycleMapper payrollCycleMapper;

    @Override
    public List<PayrollCycleDTO> saveOrUpdate(List<PayrollCycleDTO> payrollCycleDTOs) {
        return payrollCycleMapper.toDto(payrollCycleRepository.saveAll(payrollCycleMapper.toEntity(payrollCycleDTOs)));
    }

    @Override
    public List<PayrollCycleDTO> getPayrollCyclesForSchoolInfo(Long schoolInfoId, Integer year, Month month) {
        List<PayrollCycle> payrollCycles;
        if (month != null) {
            payrollCycles = payrollCycleRepository.findBySchoolInfoIdAndYearAndMonth(schoolInfoId, year, month);
        } else if (year != null) {
            payrollCycles = payrollCycleRepository.findBySchoolInfoIdAndYear(schoolInfoId, year);
        } else {
            payrollCycles = payrollCycleRepository.findBySchoolInfoId(schoolInfoId);
        }
        List<PayrollCycleDTO> result = payrollCycleMapper.toDto(payrollCycles);
        Collections.sort(result, new PayrollCycleComparator());
        return result;
    }
}
