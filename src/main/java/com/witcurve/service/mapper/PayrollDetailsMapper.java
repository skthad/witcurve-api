package com.witcurve.service.mapper;

import com.witcurve.domain.PayrollDetails;
import com.witcurve.domain.Staff;
import com.witcurve.service.dto.PayrollDetailsDTO;
import com.witcurve.service.dto.StaffDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
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

    default Staff toStaff(StaffDTO staffDTO) {
        Staff staff = new Staff();
        staff.setId(staffDTO.getId());
        return staff;
    }

    default StaffDTO toStaffDTO(Staff staff) {
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setId(staff.getId());
        return staffDTO;
    }
}
