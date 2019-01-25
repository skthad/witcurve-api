package com.witcurve.service.mapper;

import com.witcurve.domain.PayrollCycle;
import com.witcurve.service.dto.PayrollCycleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayrollCycleMapper extends EntityMapper<PayrollCycleDTO, PayrollCycle> {

    @Mapping(source = "schoolInfo.id", target = "schoolInfoId")
    PayrollCycleDTO toDto(PayrollCycle payrollCycle);

    @Mapping(source = "schoolInfoId", target = "schoolInfo.id")
    PayrollCycle toEntity(PayrollCycleDTO payrollCycleDTO);

    default PayrollCycle fromId(Long id) {
        if(id == null) {
            return null;
        }
        PayrollCycle payrollCycle = new PayrollCycle();
        payrollCycle.setId(id);
        return payrollCycle;
    }

}
