package com.witcurve.service.mapper;

import com.witcurve.domain.Payroll;
import com.witcurve.domain.enumeration.ModeOfPayment;
import com.witcurve.service.dto.PayrollCycleDTO;
import com.witcurve.service.dto.PayrollDTO;
import com.witcurve.service.dto.PayrollDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PayrollDetailsMapper.class, PayrollCycleMapper.class})
public interface PayrollMapper extends EntityMapper<PayrollDTO, Payroll> {

    PayrollDTO toDto(Payroll payroll);

    Payroll toEntity(PayrollDTO payrollDTO);

    default Payroll fromId(Long id) {
        if(id == null) {
            return null;
        }
        Payroll payroll = new Payroll();
        payroll.setId(id);
        return payroll;
    }

    default PayrollDTO fromPayrollDetails(PayrollDetailsDTO payrollDetails, PayrollCycleDTO payrollCycle) {
        PayrollDTO payrollDTO = new PayrollDTO();
        payrollDTO.setPayrollDetails(payrollDetails);
        payrollDTO.setPayrollCycle(payrollCycle);
        payrollDTO.setBasicSalary(payrollDetails.getBasicSalary());
        payrollDTO.setHouseRentAllowance(payrollDetails.getHouseRentAllowance());
        payrollDTO.setConveyanceAllowance(payrollDetails.getConveyanceAllowance());
        payrollDTO.setMedicalAllowance(payrollDetails.getMedicalAllowance());
        payrollDTO.setManagerialAllowance(payrollDetails.getManagerialAllowance());
        payrollDTO.setLeaveTravelAllowance(payrollDetails.getLeaveTravelAllowance());
        payrollDTO.setProvidentFund(payrollDetails.getProvidentFund());
        payrollDTO.setProfessionalTax(payrollDetails.getProfessionalTax());
        Integer daysInCycle = payrollCycle.getCycleEnd().until(payrollCycle.getCycleStart()).getDays() + 1;
        payrollDTO.setPayableDays(daysInCycle.doubleValue());
        payrollDTO.setPaidDays(daysInCycle.doubleValue());
        payrollDTO.setModeOfPayment(ModeOfPayment.CHEQUE);
        return payrollDTO;
    }

}
