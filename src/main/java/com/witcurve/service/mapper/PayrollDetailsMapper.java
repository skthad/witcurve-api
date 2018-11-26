package com.witcurve.service.mapper;

import com.witcurve.domain.PayrollDetails;
import com.witcurve.service.dto.PayrollDetailsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StaffMapper.class})
public interface PayrollDetailsMapper extends EntityMapper<PayrollDetailsDTO, PayrollDetails> {

    PayrollDetailsDTO toDto(PayrollDetails payrollDetails);

    PayrollDetails toEntity(PayrollDetailsDTO payrollDetailsDTO);

    default PayrollDetails fromId(Long id) {
        if(id == null) {
            return null;
        }
        PayrollDetails payrollDetails = new PayrollDetails();
        payrollDetails.setId(id);
        return payrollDetails;
    }
}
