package com.witcurve.service.mapper;

import com.witcurve.domain.Payroll;
import com.witcurve.service.dto.PayrollDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PayrollDetailsMapper.class})
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

}
