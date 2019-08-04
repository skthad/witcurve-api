package com.witcurve.service.mapper;

import com.witcurve.domain.PaymentOrder;
import com.witcurve.service.dto.PaymentOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public interface PaymentOrderMapper extends EntityMapper<PaymentOrderDTO, PaymentOrder> {

    @Mapping(target = "studentId", source = "student.id")
    PaymentOrderDTO toDto(PaymentOrder paymentOrder);

    @Mapping(target = "student", source = "studentId")
    PaymentOrder toEntity(PaymentOrderDTO paymentOrderDTO);

}
