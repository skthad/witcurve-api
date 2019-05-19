package com.witcurve.service.impl;

import com.witcurve.domain.ConfigSettings;
import com.witcurve.domain.Payroll;
import com.witcurve.domain.PayrollCycle;
import com.witcurve.domain.PayrollDetails;
import com.witcurve.domain.enumeration.ConfigFieldName;
import com.witcurve.domain.enumeration.ConfigType;
import com.witcurve.repository.ConfigSettingsRepository;
import com.witcurve.repository.PayrollCycleRepository;
import com.witcurve.repository.PayrollDetailsRepository;
import com.witcurve.repository.PayrollRepository;
import com.witcurve.service.PayrollService;
import com.witcurve.service.dto.PayrollCycleDTO;
import com.witcurve.service.dto.PayrollDTO;
import com.witcurve.service.dto.PayrollDetailsDTO;
import com.witcurve.service.mapper.PayrollCycleMapper;
import com.witcurve.service.mapper.PayrollDetailsMapper;
import com.witcurve.service.mapper.PayrollMapper;
import com.witcurve.service.util.PayrollComparator;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    PayrollCycleRepository payrollCycleRepository;

    @Autowired
    PayrollCycleMapper payrollCycleMapper;

    @Autowired
    ConfigSettingsRepository configSettingsRepository;

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
    public List<PayrollDetailsDTO> getPayrollDetailsForStaff(Long staffId, Boolean activeOnly) {
        if (activeOnly) {
            List<PayrollDetails> result = payrollDetailsRepository.findActivePayrollDetailsForActiveStaff(staffId);
            return payrollDetailsMapper.toDto(result);
        } else {
            List<PayrollDetails> result = payrollDetailsRepository.findAllPayrollDetailsForStaff(staffId);
            return payrollDetailsMapper.toDto(result);
        }

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
    public List<PayrollDTO> getPayrollsForSchoolInfo(Long schoolInfoId, Long payrollCycleId) {
        Optional<PayrollCycle> result = payrollCycleRepository.findById(payrollCycleId);
        if (!result.isPresent()) {
            throw new WitcurveException("No payroll cycle exists with ID: " + payrollCycleId);
        }
        PayrollCycle payrollCycle = result.get();
        if (!payrollCycle.getSchoolInfo().getId().equals(schoolInfoId)) {
            throw new WitcurveException("Given schoolInfoId does not match with the one in payroll Cycle");
        }
        if (!payrollCycle.getCycleEnd().isBefore(LocalDate.now())) {
            return new ArrayList<>();
        }
        PayrollCycleDTO payrollCycleDTO = payrollCycleMapper.toDto(payrollCycle);
        List<Payroll> payrolls = payrollRepository.findPayrollForScoolInfoInYearAndMonth(schoolInfoId, payrollCycleDTO.getYear(), payrollCycleDTO.getMonth());
        List<PayrollDTO> payrollDTOs = payrollMapper.toDto(payrolls);

        List<PayrollDetails> payrollDetails = payrollDetailsRepository.findPayrollDetailsForActiveStaffInSchoolInfo(schoolInfoId,  payrollCycleDTO.getYear(), payrollCycleDTO.getMonth());
        List<PayrollDetailsDTO> payrollDetailsDTOs = payrollDetailsMapper.toDto(payrollDetails);
        List<ConfigSettings> configSettings = configSettingsRepository.getConfigSettingsBySchoolIdAndTypes(schoolInfoId, new ConfigType[]{ConfigType.STAFF_ATTENDANCE});
        Integer durationInMins = 0;
        String presentStatusBefore = "";
        for (ConfigSettings cs : configSettings) {
            if (ConfigFieldName.STAFF_GRACE_DURATION_IN_MINS.equals(cs.getFieldName())) {
                durationInMins = Integer.parseInt(cs.getFieldValue());
            }
            if (ConfigFieldName.STAFF_PRESENT_STATUS_BEFORE.equals(cs.getFieldName())) {
                presentStatusBefore = cs.getFieldValue();
            }
        }
        Integer hours = Integer.parseInt(presentStatusBefore.substring(0, 2));
        Integer mins = Integer.parseInt(presentStatusBefore.substring(2));
        for (PayrollDetailsDTO pd : payrollDetailsDTOs) {
            PayrollDTO payrollDTO = payrollMapper.fromPayrollDetails(pd, payrollCycleDTO);
            payrollDTO.setPayrollCycle(payrollCycleDTO);
            //TODO: ontime days == calcualte from biometric and config logic
            //TODO: grace days == calcualte from biometric and config logic
            //TODO: late days == calculatte from biometric and config logic
            payrollDTO.setOnTimeDays(14);
            payrollDTO.setGraceDays(1);
            payrollDTO.setLateDays(3);
            payrollDTOs.add(payrollDTO);
        }
        return payrollDTOs;
    }
}
