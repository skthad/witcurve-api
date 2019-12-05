package com.witcurve.service.mapper;

import com.witcurve.domain.SessionFeeDescription;
import com.witcurve.service.dto.SessionFeeDescriptionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FeeDetailsMapper.class})
public interface SessionFeeDescriptionMapper extends EntityMapper<SessionFeeDescriptionDTO, SessionFeeDescription> {

    @Mapping(source = "feeDescriptionId", target = "feeDescription.id")
    SessionFeeDescription toEntity(SessionFeeDescriptionDTO sessionFeeDescriptionDTO);


    @Mapping(target = "feeDescriptionId", source = "feeDescription.id")
    SessionFeeDescriptionDTO toDto(SessionFeeDescription sessionFeeDescription);

    default SessionFeeDescription fromId(Long id) {
        if (id == null) {
            return null;
        }
        SessionFeeDescription sessionFeeDescription = new SessionFeeDescription();
        sessionFeeDescription.setId(id);
        return sessionFeeDescription;
    }
}
