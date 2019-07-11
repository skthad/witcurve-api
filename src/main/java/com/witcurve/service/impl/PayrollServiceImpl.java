package com.witcurve.service.impl;

import com.witcurve.domain.Event;
import com.witcurve.domain.Payroll;
import com.witcurve.domain.PayrollCycle;
import com.witcurve.domain.PayrollDetails;
import com.witcurve.repository.*;
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
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

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

    @Autowired
    EventRepository eventRepository;

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

        Map<Long, PayrollDTO> payrollMap = new HashMap<>();
        for (PayrollDTO payrollDTO : payrollDTOs) {
            payrollMap.put(payrollDTO.getPayrollDetails().getStaff().getId(), payrollDTO);
        }
        List<PayrollDetails> payrollDetails = payrollDetailsRepository.findPayrollDetailsForActiveStaffInSchoolInfo(schoolInfoId,  payrollCycleDTO.getYear(), payrollCycleDTO.getMonth());
        List<PayrollDetailsDTO> payrollDetailsDTOs = payrollDetailsMapper.toDto(payrollDetails);

        Double payableDays = Double.valueOf(DAYS.between(payrollCycleDTO.getCycleStart(), payrollCycleDTO.getCycleEnd()) + 1);
        Map<Long, Integer> presentCount = new HashMap<>();
        Map<Long, Integer> absentCount = new HashMap<>();
        Map<Long, Integer> halfDayCount = new HashMap<>();
        Map<Long, Integer> lateCount = new HashMap<>();
        Map<Long, Integer> graceCount = new HashMap<>();
        List<Event> attendanceEvents = eventRepository.findAttendanceForAllStaffInSchoolInfo(payrollCycle.getCycleStart(), payrollCycle.getCycleEnd(), schoolInfoId);

        for (Event attendanceEvent : attendanceEvents) {
            Long staffId = attendanceEvent.getStaff().getId();
            if (presentCount.get(staffId) == null) {
                presentCount.put(staffId, 0);
            }
            if (absentCount.get(staffId) == null) {
                absentCount.put(staffId, 0);
            }
            if (halfDayCount.get(staffId) == null) {
                halfDayCount.put(staffId, 0);
            }
            if (lateCount.get(staffId) == null) {
                lateCount.put(staffId, 0);
            }
            if (graceCount.get(staffId) == null) {
                graceCount.put(staffId, 0);
            }
            switch (attendanceEvent.getAttendanceType()) {
                case PRESENT:
                    presentCount.put(staffId, presentCount.get(staffId) + 1);
                    break;
                case GRACE:
                    graceCount.put(staffId, graceCount.get(staffId) + 1);
                    break;
                case LATE:
                    lateCount.put(staffId, lateCount.get(staffId) + 1);
                    break;
                case ABSENT:
                    absentCount.put(staffId, absentCount.get(staffId) + 1);
                    break;
                case HALF_DAY:
                    halfDayCount.put(staffId, halfDayCount.get(staffId) + 1);
                    break;
                default:
            }
        }
        for (PayrollDetailsDTO pd : payrollDetailsDTOs) {
            Long staffId = pd.getStaff().getId();
            if (payrollMap.get(staffId) != null) {
                payrollDTOs.add(payrollMap.get(staffId));
                continue;
            }
            PayrollDTO payrollDTO = payrollMapper.fromPayrollDetails(pd, payrollCycleDTO);
            payrollDTO.setPayrollCycle(payrollCycleDTO);
            payrollDTO.setPayableDays(payableDays);
            payrollDTO.setPaidDays(payableDays);
            payrollDTO.setOnTimeDays(presentCount.get(staffId) == null ? 0 : presentCount.get(staffId));
            payrollDTO.setGraceDays(graceCount.get(staffId) == null ? 0 : graceCount.get(staffId));
            payrollDTO.setLateDays(lateCount.get(staffId) == null ? 0 : lateCount.get(staffId));
            payrollDTO.setAbsentDays(absentCount.get(staffId) == null ? 0 : absentCount.get(staffId));
            payrollDTO.setHalfDays(halfDayCount.get(staffId) == null ? 0 : halfDayCount.get(staffId));
            payrollDTO.setNonWorkingDays(payableDays - (
                payrollDTO.getOnTimeDays() +
                payrollDTO.getGraceDays() +
                payrollDTO.getLateDays() +
                payrollDTO.getAbsentDays() +
                payrollDTO.getHalfDays())
            );
            payrollDTOs.add(payrollDTO);
        }
        return payrollDTOs;
    }
}
